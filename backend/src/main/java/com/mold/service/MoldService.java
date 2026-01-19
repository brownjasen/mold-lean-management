package com.mold.service;

import com.mold.dto.MoldStatistics;
import com.mold.entity.Mold;
import com.mold.entity.Mold.MoldStatus;
import com.mold.repository.MoldRepository;
import com.mold.repository.ProcessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MoldService {

    private final MoldRepository moldRepository;
    private final ProcessRepository processRepository;

    public List<Mold> getAllMolds() {
        return moldRepository.findByOrderByOrderTimeDesc();
    }

    public Mold getMoldById(Long id) {
        return moldRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("模具不存在，id: " + id));
    }

    public Mold getMoldByCode(String moldCode) {
        return moldRepository.findByMoldCode(moldCode)
                .orElseThrow(() -> new RuntimeException("模具不存在: " + moldCode));
    }

    @Transactional
    public Mold createMold(Mold mold) {
        if (moldRepository.existsByMoldCode(mold.getMoldCode())) {
            throw new RuntimeException("模具编号已存在: " + mold.getMoldCode());
        }
        if (mold.getStatus() == null) {
            mold.setStatus(MoldStatus.PENDING);
        }
        if (mold.getProgress() == null) {
            mold.setProgress(0);
        }
        return moldRepository.save(mold);
    }

    @Transactional
    public Mold updateMold(Long id, Mold moldDetails) {
        Mold mold = moldRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("模具不存在，id: " + id));

        mold.setMoldCode(moldDetails.getMoldCode());
        mold.setPriority(moldDetails.getPriority());
        mold.setStatus(moldDetails.getStatus());
        mold.setProgress(moldDetails.getProgress());
        mold.setOrderTime(moldDetails.getOrderTime());
        mold.setStartProductionTime(moldDetails.getStartProductionTime());
        mold.setCompletionTime(moldDetails.getCompletionTime());

        return moldRepository.save(mold);
    }

    @Transactional
    public void deleteMold(Long id) {
        Mold mold = moldRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("模具不存在，id: " + id));
        moldRepository.delete(mold);
    }

    public MoldStatistics getStatistics() {
        MoldStatistics stats = new MoldStatistics();
        stats.setTotalMolds(moldRepository.count());
        stats.setProcessingMolds(moldRepository.countByStatus(MoldStatus.PROCESSING));
        stats.setCompletedMolds(moldRepository.countByStatus(MoldStatus.COMPLETED));
        stats.setPendingMolds(moldRepository.countByStatus(MoldStatus.PENDING));
        return stats;
    }

    @Transactional
    public Mold startProduction(Long id) {
        Mold mold = moldRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("模具不存在，id: " + id));

        if (mold.getStatus() == MoldStatus.PROCESSING) {
            throw new RuntimeException("模具已在生产中");
        }
        if (mold.getStatus() == MoldStatus.COMPLETED) {
            throw new RuntimeException("模具已完成，无法开始生产");
        }

        mold.setStatus(MoldStatus.PROCESSING);
        mold.setStartProductionTime(LocalDateTime.now());
        return moldRepository.save(mold);
    }

    @Transactional
    public Mold completeProduction(Long id) {
        Mold mold = moldRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("模具不存在，id: " + id));

        if (mold.getStatus() == MoldStatus.COMPLETED) {
            throw new RuntimeException("模具已完成");
        }
        if (mold.getStatus() == MoldStatus.PENDING) {
            throw new RuntimeException("模具未开始生产");
        }

        mold.setStatus(MoldStatus.COMPLETED);
        mold.setProgress(100);
        mold.setCompletionTime(LocalDateTime.now());
        return moldRepository.save(mold);
    }

    @Transactional
    public Mold updateProgress(Long id, Integer progress) {
        Mold mold = moldRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("模具不存在，id: " + id));

        if (progress < 0 || progress > 100) {
            throw new RuntimeException("进度必须在0-100之间");
        }

        mold.setProgress(progress);

        // 根据进度自动更新状态
        if (progress == 100 && mold.getStatus() != MoldStatus.COMPLETED) {
            mold.setStatus(MoldStatus.COMPLETED);
            mold.setCompletionTime(LocalDateTime.now());
        } else if (progress > 0 && mold.getStatus() == MoldStatus.PENDING) {
            mold.setStatus(MoldStatus.PROCESSING);
            if (mold.getStartProductionTime() == null) {
                mold.setStartProductionTime(LocalDateTime.now());
            }
        }

        return moldRepository.save(mold);
    }
}

package com.mold.service;

import com.mold.entity.Process;
import com.mold.entity.Process.ModuleType;
import com.mold.entity.Process.ProcessStatus;
import com.mold.repository.ProcessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcessService {

    private final ProcessRepository processRepository;

    public List<Process> getProcessesByMoldId(Long moldId) {
        return processRepository.findByMoldIdOrderByCreatedAtAsc(moldId);
    }

    public List<Process> getProcessesByMoldIdAndModule(Long moldId, ModuleType moduleType) {
        return processRepository.findByMoldIdAndModuleType(moldId, moduleType);
    }

    public Process getProcessById(Long id) {
        return processRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("工序不存在，id: " + id));
    }

    @Transactional
    public Process createProcess(Process process) {
        if (process.getStatus() == null) {
            process.setStatus(ProcessStatus.PENDING);
        }
        return processRepository.save(process);
    }

    @Transactional
    public Process updateProcess(Long id, Process processDetails) {
        Process process = processRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("工序不存在，id: " + id));

        process.setName(processDetails.getName());
        process.setModuleType(processDetails.getModuleType());
        process.setOperator(processDetails.getOperator());
        process.setStatus(processDetails.getStatus());

        return processRepository.save(process);
    }

    @Transactional
    public void deleteProcess(Long id) {
        Process process = processRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("工序不存在，id: " + id));
        processRepository.delete(process);
    }

    @Transactional
    public Process startProcess(Long id) {
        Process process = processRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("工序不存在，id: " + id));

        if (process.getStatus() == ProcessStatus.PROCESSING) {
            throw new RuntimeException("工序已在进行中");
        }
        if (process.getStatus() == ProcessStatus.COMPLETED) {
            throw new RuntimeException("工序已完成");
        }

        process.setStatus(ProcessStatus.PROCESSING);
        process.setStartTime(LocalDateTime.now());
        return processRepository.save(process);
    }

    @Transactional
    public Process completeProcess(Long id) {
        Process process = processRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("工序不存在，id: " + id));

        if (process.getStatus() == ProcessStatus.COMPLETED) {
            throw new RuntimeException("工序已完成");
        }
        if (process.getStatus() == ProcessStatus.PENDING) {
            throw new RuntimeException("工序未开始");
        }

        process.setStatus(ProcessStatus.COMPLETED);
        process.setEndTime(LocalDateTime.now());
        return processRepository.save(process);
    }
}

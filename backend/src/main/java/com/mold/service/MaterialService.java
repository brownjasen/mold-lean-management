package com.mold.service;

import com.mold.dto.MaterialStatistics;
import com.mold.entity.Material;
import com.mold.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialService {

    private final MaterialRepository materialRepository;

    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }

    public Material getMaterialById(Long id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("物料不存在，id: " + id));
    }

    public Material getMaterialByCode(String code) {
        return materialRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("物料不存在: " + code));
    }

    @Transactional
    public Material createMaterial(Material material) {
        if (materialRepository.existsByCode(material.getCode())) {
            throw new RuntimeException("物料编号已存在: " + material.getCode());
        }
        if (material.getCurrentStock() == null) {
            material.setCurrentStock(0);
        }
        if (material.getSafetyStock() == null) {
            material.setSafetyStock(0);
        }
        return materialRepository.save(material);
    }

    @Transactional
    public Material updateMaterial(Long id, Material materialDetails) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("物料不存在，id: " + id));

        material.setCode(materialDetails.getCode());
        material.setName(materialDetails.getName());
        material.setSpecification(materialDetails.getSpecification());
        material.setUnit(materialDetails.getUnit());
        material.setSafetyStock(materialDetails.getSafetyStock());

        return materialRepository.save(material);
    }

    @Transactional
    public void deleteMaterial(Long id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("物料不存在，id: " + id));
        materialRepository.delete(material);
    }

    public MaterialStatistics getStatistics() {
        MaterialStatistics stats = new MaterialStatistics();
        stats.setTotalMaterials(materialRepository.count());
        stats.setLowStockCount(materialRepository.countByCurrentStockLessThanSafetyStock());
        stats.setNormalStockCount(materialRepository.count() - stats.getLowStockCount());
        return stats;
    }

    @Transactional
    public Material inStock(Long id, Integer quantity) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("物料不存在，id: " + id));

        if (quantity == null || quantity <= 0) {
            throw new RuntimeException("入库数量必须大于0");
        }

        material.setCurrentStock(material.getCurrentStock() + quantity);
        return materialRepository.save(material);
    }

    @Transactional
    public Material outStock(Long id, Integer quantity) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("物料不存在，id: " + id));

        if (quantity == null || quantity <= 0) {
            throw new RuntimeException("出库数量必须大于0");
        }

        if (material.getCurrentStock() < quantity) {
            throw new RuntimeException("库存不足，当前库存: " + material.getCurrentStock());
        }

        material.setCurrentStock(material.getCurrentStock() - quantity);
        return materialRepository.save(material);
    }

    public List<Material> searchMaterials(String keyword) {
        return materialRepository.findByCodeContainingOrNameContaining(keyword, keyword);
    }
}

package com.mold.repository;

import com.mold.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {

    List<Material> findByCodeContainingOrNameContaining(String code, String name);

    java.util.Optional<Material> findByCode(String code);

    boolean existsByCode(String code);

    @Query("SELECT m FROM Material m WHERE m.currentStock < m.safetyStock")
    List<Material> findLowStockMaterials();

    @Query("SELECT m FROM Material m WHERE m.currentStock < m.safetyStock * 2")
    List<Material> findWarningStockMaterials();

    @Query("SELECT COUNT(m) FROM Material m WHERE m.currentStock < m.safetyStock")
    long countByCurrentStockLessThanSafetyStock();
}

package com.mold.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "material")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 200)
    private String specification;

    @Column(length = 20)
    private String unit;

    @Column(name = "current_stock", nullable = false)
    private Integer currentStock = 0;

    @Column(name = "safety_stock", nullable = false)
    private Integer safetyStock = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public StockStatus getStockStatus() {
        if (currentStock < safetyStock) {
            return StockStatus.LOW;
        } else if (currentStock < safetyStock * 2) {
            return StockStatus.WARNING;
        } else {
            return StockStatus.NORMAL;
        }
    }

    public enum StockStatus {
        LOW,      // 库存不足
        WARNING,  // 库存偏低
        NORMAL    // 库存充足
    }
}

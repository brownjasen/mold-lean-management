package com.mold.dto;

import lombok.Data;

@Data
public class MaterialStatistics {
    private Long totalMaterials;
    private Long lowStockCount;
    private Long normalStockCount;
}

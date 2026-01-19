package com.mold.dto;

import lombok.Data;

@Data
public class MoldStatistics {
    private Long totalMolds;
    private Long processingMolds;
    private Long completedMolds;
    private Long pendingMolds;
}

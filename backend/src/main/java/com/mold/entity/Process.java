package com.mold.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "process")
public class Process {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mold_id", nullable = false)
    @JsonIgnore
    private Mold mold;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private ModuleType moduleType;

    @Column(length = 50)
    private String operator;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ProcessStatus status = ProcessStatus.PENDING;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

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

    public enum ModuleType {
        FRAME,       // 模架
        MAIN_PARTS,  // 三大件
        ACCESSORIES, // 辅料
        ASSEMBLY,    // 组装
        TESTING,     // 试模
        REPAIR       // 返修
    }

    public enum ProcessStatus {
        PENDING,
        PROCESSING,
        COMPLETED
    }
}

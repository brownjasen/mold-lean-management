package com.mold.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "mold")
public class Mold {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String moldCode;

    @Column(nullable = false)
    private Integer priority = 1;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private MoldStatus status = MoldStatus.PENDING;

    @Column(nullable = false)
    private Integer progress = 0;

    @Column(nullable = false, name = "order_time")
    private LocalDateTime orderTime;

    @Column(name = "start_production_time")
    private LocalDateTime startProductionTime;

    @Column(name = "completion_time")
    private LocalDateTime completionTime;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "mold", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Process> processes = new ArrayList<>();

    @OneToMany(mappedBy = "mold", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Blueprint> blueprints = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (orderTime == null) {
            orderTime = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum MoldStatus {
        PENDING,
        PROCESSING,
        COMPLETED
    }
}

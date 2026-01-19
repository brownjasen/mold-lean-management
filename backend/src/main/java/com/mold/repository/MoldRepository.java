package com.mold.repository;

import com.mold.entity.Mold;
import com.mold.entity.Mold.MoldStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoldRepository extends JpaRepository<Mold, Long> {

    List<Mold> findByStatus(MoldStatus status);

    List<Mold> findByOrderByOrderTimeDesc();

    @Query("SELECT m FROM Mold m LEFT JOIN FETCH m.processes WHERE m.id = :id")
    Mold findByIdWithProcesses(Long id);

    @Query("SELECT m FROM Mold m LEFT JOIN FETCH m.blueprints WHERE m.id = :id")
    Mold findByIdWithBlueprints(Long id);

    @Query("SELECT m FROM Mold m LEFT JOIN FETCH m.processes LEFT JOIN FETCH m.blueprints WHERE m.id = :id")
    Mold findByIdWithAll(Long id);

    boolean existsByMoldCode(String moldCode);

    java.util.Optional<Mold> findByMoldCode(String moldCode);

    long countByStatus(MoldStatus status);
}

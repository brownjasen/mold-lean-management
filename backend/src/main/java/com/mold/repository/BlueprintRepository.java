package com.mold.repository;

import com.mold.entity.Blueprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlueprintRepository extends JpaRepository<Blueprint, Long> {

    List<Blueprint> findByMoldId(Long moldId);

    List<Blueprint> findByMoldIdOrderByCreatedAtDesc(Long moldId);

    List<Blueprint> findByMoldIdAndVersion(Long moldId, String version);
}

package com.mold.repository;

import com.mold.entity.Process;
import com.mold.entity.Process.ProcessStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessRepository extends JpaRepository<Process, Long> {

    List<Process> findByMoldId(Long moldId);

    List<Process> findByMoldIdOrderByCreatedAtAsc(Long moldId);

    List<Process> findByMoldIdAndModuleType(Long moldId, Process.ModuleType moduleType);

    List<Process> findByStatus(ProcessStatus status);

    long countByMoldIdAndStatus(Long moldId, ProcessStatus status);
}

package com.mold.controller;

import com.mold.dto.ApiResponse;
import com.mold.entity.Process;
import com.mold.entity.Process.ModuleType;
import com.mold.service.ProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/processes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProcessController {

    private final ProcessService processService;

    @GetMapping("/mold/{moldId}")
    public ApiResponse<List<Process>> getProcessesByMoldId(@PathVariable Long moldId) {
        return ApiResponse.success(processService.getProcessesByMoldId(moldId));
    }

    @GetMapping("/mold/{moldId}/module/{moduleType}")
    public ApiResponse<List<Process>> getProcessesByMoldIdAndModule(
            @PathVariable Long moldId,
            @PathVariable ModuleType moduleType) {
        return ApiResponse.success(processService.getProcessesByMoldIdAndModule(moldId, moduleType));
    }

    @GetMapping("/{id}")
    public ApiResponse<Process> getProcessById(@PathVariable Long id) {
        return ApiResponse.success(processService.getProcessById(id));
    }

    @PostMapping
    public ApiResponse<Process> createProcess(@Valid @RequestBody Process process) {
        return ApiResponse.success("工序创建成功", processService.createProcess(process));
    }

    @PutMapping("/{id}")
    public ApiResponse<Process> updateProcess(@PathVariable Long id, @Valid @RequestBody Process process) {
        return ApiResponse.success("工序更新成功", processService.updateProcess(id, process));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProcess(@PathVariable Long id) {
        processService.deleteProcess(id);
        return ApiResponse.success("工序删除成功", null);
    }

    @PostMapping("/{id}/start")
    public ApiResponse<Process> startProcess(@PathVariable Long id) {
        return ApiResponse.success("工序开始成功", processService.startProcess(id));
    }

    @PostMapping("/{id}/complete")
    public ApiResponse<Process> completeProcess(@PathVariable Long id) {
        return ApiResponse.success("工序完成成功", processService.completeProcess(id));
    }
}

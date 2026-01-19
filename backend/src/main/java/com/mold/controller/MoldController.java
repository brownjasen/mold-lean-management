package com.mold.controller;

import com.mold.dto.ApiResponse;
import com.mold.dto.MoldStatistics;
import com.mold.entity.Mold;
import com.mold.service.MoldService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/molds")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MoldController {

    private final MoldService moldService;

    @GetMapping
    public ApiResponse<List<Mold>> getAllMolds() {
        return ApiResponse.success(moldService.getAllMolds());
    }

    @GetMapping("/{id}")
    public ApiResponse<Mold> getMoldById(@PathVariable Long id) {
        return ApiResponse.success(moldService.getMoldById(id));
    }

    @GetMapping("/code/{moldCode}")
    public ApiResponse<Mold> getMoldByCode(@PathVariable String moldCode) {
        return ApiResponse.success(moldService.getMoldByCode(moldCode));
    }

    @GetMapping("/statistics")
    public ApiResponse<MoldStatistics> getStatistics() {
        return ApiResponse.success(moldService.getStatistics());
    }

    @PostMapping
    public ApiResponse<Mold> createMold(@Valid @RequestBody Mold mold) {
        return ApiResponse.success("模具创建成功", moldService.createMold(mold));
    }

    @PutMapping("/{id}")
    public ApiResponse<Mold> updateMold(@PathVariable Long id, @Valid @RequestBody Mold mold) {
        return ApiResponse.success("模具更新成功", moldService.updateMold(id, mold));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMold(@PathVariable Long id) {
        moldService.deleteMold(id);
        return ApiResponse.success("模具删除成功", null);
    }

    @PostMapping("/{id}/start")
    public ApiResponse<Mold> startProduction(@PathVariable Long id) {
        return ApiResponse.success("开始生产成功", moldService.startProduction(id));
    }

    @PostMapping("/{id}/complete")
    public ApiResponse<Mold> completeProduction(@PathVariable Long id) {
        return ApiResponse.success("生产完成成功", moldService.completeProduction(id));
    }

    @PutMapping("/{id}/progress")
    public ApiResponse<Mold> updateProgress(@PathVariable Long id, @RequestParam Integer progress) {
        return ApiResponse.success("进度更新成功", moldService.updateProgress(id, progress));
    }
}

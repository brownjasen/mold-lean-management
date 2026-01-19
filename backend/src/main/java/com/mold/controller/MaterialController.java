package com.mold.controller;

import com.mold.dto.ApiResponse;
import com.mold.dto.MaterialStatistics;
import com.mold.dto.MaterialTransactionRequest;
import com.mold.entity.Material;
import com.mold.service.MaterialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/materials")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MaterialController {

    private final MaterialService materialService;

    @GetMapping
    public ApiResponse<List<Material>> getAllMaterials() {
        return ApiResponse.success(materialService.getAllMaterials());
    }

    @GetMapping("/{id}")
    public ApiResponse<Material> getMaterialById(@PathVariable Long id) {
        return ApiResponse.success(materialService.getMaterialById(id));
    }

    @GetMapping("/code/{code}")
    public ApiResponse<Material> getMaterialByCode(@PathVariable String code) {
        return ApiResponse.success(materialService.getMaterialByCode(code));
    }

    @GetMapping("/statistics")
    public ApiResponse<MaterialStatistics> getStatistics() {
        return ApiResponse.success(materialService.getStatistics());
    }

    @GetMapping("/search")
    public ApiResponse<List<Material>> searchMaterials(@RequestParam String keyword) {
        return ApiResponse.success(materialService.searchMaterials(keyword));
    }

    @PostMapping
    public ApiResponse<Material> createMaterial(@Valid @RequestBody Material material) {
        return ApiResponse.success("物料创建成功", materialService.createMaterial(material));
    }

    @PutMapping("/{id}")
    public ApiResponse<Material> updateMaterial(@PathVariable Long id, @Valid @RequestBody Material material) {
        return ApiResponse.success("物料更新成功", materialService.updateMaterial(id, material));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMaterial(@PathVariable Long id) {
        materialService.deleteMaterial(id);
        return ApiResponse.success("物料删除成功", null);
    }

    @PostMapping("/{id}/in")
    public ApiResponse<Material> inStock(@PathVariable Long id, @Valid @RequestBody MaterialTransactionRequest request) {
        return ApiResponse.success("入库成功", materialService.inStock(id, request.getQuantity()));
    }

    @PostMapping("/{id}/out")
    public ApiResponse<Material> outStock(@PathVariable Long id, @Valid @RequestBody MaterialTransactionRequest request) {
        return ApiResponse.success("出库成功", materialService.outStock(id, request.getQuantity()));
    }
}

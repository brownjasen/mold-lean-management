package com.mold.controller;

import com.mold.dto.ApiResponse;
import com.mold.entity.Blueprint;
import com.mold.service.BlueprintService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/blueprints")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BlueprintController {

    private final BlueprintService blueprintService;

    @GetMapping("/mold/{moldId}")
    public ApiResponse<List<Blueprint>> getBlueprintsByMoldId(@PathVariable Long moldId) {
        return ApiResponse.success(blueprintService.getBlueprintsByMoldId(moldId));
    }

    @GetMapping("/{id}")
    public ApiResponse<Blueprint> getBlueprintById(@PathVariable Long id) {
        return ApiResponse.success(blueprintService.getBlueprintById(id));
    }

    @PostMapping
    public ApiResponse<Blueprint> createBlueprint(@Valid @RequestBody Blueprint blueprint) {
        return ApiResponse.success("图纸创建成功", blueprintService.createBlueprint(blueprint));
    }

    @PutMapping("/{id}")
    public ApiResponse<Blueprint> updateBlueprint(@PathVariable Long id, @Valid @RequestBody Blueprint blueprint) {
        return ApiResponse.success("图纸更新成功", blueprintService.updateBlueprint(id, blueprint));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteBlueprint(@PathVariable Long id) {
        blueprintService.deleteBlueprint(id);
        return ApiResponse.success("图纸删除成功", null);
    }
}

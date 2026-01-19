package com.mold.service;

import com.mold.entity.Blueprint;
import com.mold.repository.BlueprintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlueprintService {

    private final BlueprintRepository blueprintRepository;

    public List<Blueprint> getBlueprintsByMoldId(Long moldId) {
        return blueprintRepository.findByMoldIdOrderByCreatedAtDesc(moldId);
    }

    public Blueprint getBlueprintById(Long id) {
        return blueprintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("图纸不存在，id: " + id));
    }

    @Transactional
    public Blueprint createBlueprint(Blueprint blueprint) {
        return blueprintRepository.save(blueprint);
    }

    @Transactional
    public Blueprint updateBlueprint(Long id, Blueprint blueprintDetails) {
        Blueprint blueprint = blueprintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("图纸不存在，id: " + id));

        blueprint.setName(blueprintDetails.getName());
        blueprint.setVersion(blueprintDetails.getVersion());
        blueprint.setUrl(blueprintDetails.getUrl());

        return blueprintRepository.save(blueprint);
    }

    @Transactional
    public void deleteBlueprint(Long id) {
        Blueprint blueprint = blueprintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("图纸不存在，id: " + id));
        blueprintRepository.delete(blueprint);
    }
}

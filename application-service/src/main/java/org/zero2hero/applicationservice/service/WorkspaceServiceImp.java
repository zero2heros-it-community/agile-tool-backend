package org.zero2hero.applicationservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.zero2hero.applicationservice.dto.WorkspaceCreateDto;
import org.zero2hero.applicationservice.dto.WorkspaceViewDto;
import org.zero2hero.applicationservice.entity.Workspace;
import org.zero2hero.applicationservice.repository.WorkspaceRepository;

@Service
public class WorkspaceServiceImp implements WorkspaceService {
    private final KafkaTemplate kafkaTemplate;

    public WorkspaceServiceImp(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Override
    public WorkspaceViewDto create(WorkspaceCreateDto workspaceCreateDto) {


        Workspace workspace = new Workspace();
        workspace.setName(workspaceCreateDto.getName());
        workspace = workspaceRepository.save(workspace);
        System.out.println("Worspace" + workspace);
        this.kafkaTemplate.send("first_topic", "user-key", workspace);
        return WorkspaceViewDto.of(workspace);
    }
}

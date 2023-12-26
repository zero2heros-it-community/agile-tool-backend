package org.zero2hero.applicationservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.zero2hero.applicationservice.dto.WorkspaceCreateDto;
import org.zero2hero.applicationservice.dto.WorkspaceViewDto;
import org.zero2hero.applicationservice.entity.Workspace;
import org.zero2hero.applicationservice.exception.AlreadyExistException;
import org.zero2hero.applicationservice.exception.NameFormatException;
import org.zero2hero.applicationservice.exception.NotFoundException;
import org.zero2hero.applicationservice.repository.WorkspaceRepository;

import java.util.regex.Pattern;

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

        if (!isNameRightFormat(workspaceCreateDto.getName())) {
            throw new NameFormatException("workspace name is in incorrect format");
        }
        if(workspaceRepository.findByName(workspaceCreateDto.getName()).isPresent()){
            throw new AlreadyExistException(" workspace is already exist");
        }

        Workspace workspace = new Workspace();
        workspace.setName(workspaceCreateDto.getName());
        workspace = workspaceRepository.save(workspace);
        System.out.println("Workspace" + workspace);
        this.kafkaTemplate.send("first_topic", "user-key", workspace);
        return WorkspaceViewDto.of(workspace);
    }

    @Override
    public Workspace getById(Long id) {
        return workspaceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Workspace not found with this: " + id));
    }

    private boolean isNameRightFormat(String name) {
        Pattern pattern = Pattern.compile("^[a-z]+$");
        return pattern.matcher(name).matches();
    }
}

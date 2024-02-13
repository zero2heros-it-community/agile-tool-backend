package org.zero2hero.applicationservice.service;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.zero2hero.applicationservice.dto.WorkspaceCreateDto;
import org.zero2hero.applicationservice.dto.WorkspaceViewDto;
import org.zero2hero.applicationservice.entity.Workspace;
import org.zero2hero.applicationservice.exception.AlreadyExistException;
import org.zero2hero.applicationservice.exception.IdFormatException;
import org.zero2hero.applicationservice.exception.NameFormatException;
import org.zero2hero.applicationservice.exception.NotFoundException;
import org.zero2hero.applicationservice.repository.WorkspaceRepository;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class WorkspaceServiceImp implements WorkspaceService {
    private final KafkaTemplate kafkaTemplate;

    @Autowired
    public WorkspaceServiceImp(KafkaTemplate kafkaTemplate, WorkspaceRepository workspaceRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.workspaceRepository = workspaceRepository;
    }

//    @Autowired
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

    public Workspace findWorkspaceById(Long id) {
        if (id == null || id <= 0) {
            throw new IdFormatException("Workspace ID is in incorrect format");
        }
        Workspace workspace = workspaceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Workspace not found with ID: " + id));
        return workspace;
    }

    @Override
    public List<Workspace> getAll() {
        return workspaceRepository.findAll();
    }

    private boolean isNameRightFormat(String name) {
        Pattern pattern = Pattern.compile("^[a-z]+$");
        return pattern.matcher(name).matches();
    }

    public void deleteWorkspaceById(Long id){
        if(!isValidIdFormat(id)){
            throw new IdFormatException("Id is in incorrect format");
        }
        Workspace workspace = workspaceRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Workspace is not found"));
      workspaceRepository.delete(workspace);
    }
    private boolean isValidIdFormat(Long id){
        return id != null && id>0;
    }
}

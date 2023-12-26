package org.zero2hero.applicationservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zero2hero.applicationservice.dto.WorkspaceCreateDto;
import org.zero2hero.applicationservice.dto.WorkspaceViewDto;
import org.zero2hero.applicationservice.entity.Workspace;
import org.zero2hero.applicationservice.exception.NotFoundException;
import org.zero2hero.applicationservice.service.WorkspaceService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/work-space")
public class WorkspaceController {

    @Autowired
    WorkspaceService workspaceService;

    @PostMapping
    public ResponseEntity<WorkspaceViewDto> createWorkspace(@RequestBody WorkspaceCreateDto workspaceCreateDto) {
        WorkspaceViewDto workspaceViewDto = workspaceService.create(
                workspaceCreateDto);
        return new ResponseEntity<>(workspaceViewDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWorkspace(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return new ResponseEntity<>(Map.of("message", "workspace Id is in incorrect format!",
            "code", HttpStatus.BAD_REQUEST.value()),HttpStatus.BAD_REQUEST);
        }
        try {
            Workspace workspace = workspaceService.getById(id);
            return new ResponseEntity<>(workspace, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(Map.of("message", "Workspace not found"), HttpStatus.NOT_FOUND);
        }

    }

}

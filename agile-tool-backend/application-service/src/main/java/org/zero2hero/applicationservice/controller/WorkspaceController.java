package org.zero2hero.applicationservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zero2hero.applicationservice.dto.WorkspaceCreateDto;
import org.zero2hero.applicationservice.dto.WorkspaceViewDto;
import org.zero2hero.applicationservice.entity.Workspace;
import org.zero2hero.applicationservice.service.WorkspaceService;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/application/api/v1/work-space")
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
    public ResponseEntity<WorkspaceViewDto> getWorkspace(@PathVariable Long id) {
            Workspace workspace = workspaceService.findWorkspaceById(id);
            return new ResponseEntity<>(WorkspaceViewDto.of(workspace), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,Boolean>> deleteWorkspace(@PathVariable Long id){
        workspaceService.deleteWorkspaceById(id);
       Map<String,Boolean> map = new HashMap<>();
       map.put("Workspace is successfully deleted",true);
       return new ResponseEntity<>(map,HttpStatus.OK);
    }

}

package org.zero2hero.applicationservice.service;


import org.zero2hero.applicationservice.dto.WorkspaceCreateDto;
import org.zero2hero.applicationservice.dto.WorkspaceViewDto;

public interface WorkspaceService {
    WorkspaceViewDto create(WorkspaceCreateDto workspaceCreateDto);
}
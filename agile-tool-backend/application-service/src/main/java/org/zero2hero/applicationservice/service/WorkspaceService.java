package org.zero2hero.applicationservice.service;


import org.zero2hero.applicationservice.dto.WorkspaceCreateDto;
import org.zero2hero.applicationservice.dto.WorkspaceViewDto;
import org.zero2hero.applicationservice.entity.Workspace;

import java.util.List;

public interface WorkspaceService {
    WorkspaceViewDto create(WorkspaceCreateDto workspaceCreateDto);

    Workspace findWorkspaceById(Long id);

    List<Workspace> getAll();

    WorkspaceViewDto update(WorkspaceCreateDto workspaceCreateDto);

    void deleteWorkspaceById(Long id);

    public List<Workspace> getWorkspacesOfUser();
}
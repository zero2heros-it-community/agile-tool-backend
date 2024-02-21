package org.zero2hero.applicationservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zero2hero.applicationservice.dto.WorkspaceCreateDto;
import org.zero2hero.applicationservice.dto.WorkspaceViewDto;
import org.zero2hero.applicationservice.entity.Workspace;
import org.zero2hero.applicationservice.exception.IdFormatException;
import org.zero2hero.applicationservice.exception.NameFormatException;
import org.zero2hero.applicationservice.exception.NotFoundException;
import org.zero2hero.applicationservice.repository.WorkspaceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkspaceServiceImpTest {

    @Mock
    private WorkspaceRepository workspaceRepository;

    @InjectMocks
    private WorkspaceServiceImp workspaceServiceImp;

    @Test
    void getById_ShouldReturnWorkspace_WhenIdIsValid() {
        // Arrange
        Long validId = 1L;
        Workspace expectedWorkspace = new Workspace();
        expectedWorkspace.setId(validId);
        expectedWorkspace.setName("testWorkspace");

        when(workspaceRepository.findById(validId)).thenReturn(Optional.of(expectedWorkspace));

        // Act
        Workspace actualWorkspace = workspaceServiceImp.findWorkspaceById(validId);

        // Assert
        assertNotNull(actualWorkspace);
        assertEquals(expectedWorkspace.getName(), actualWorkspace.getName());
        assertEquals(expectedWorkspace.getId(), expectedWorkspace.getId());
    }

    @Test
    void getById_ShouldThrowIdFormatException_WhenIdIsInvalid() {
        // Arrange
        Long invalidId = -1L;

        // Act & Assert
        assertThrows(IdFormatException.class, () -> {
            workspaceServiceImp.findWorkspaceById(invalidId);
        });
    }

    @Test
    void getById_ShouldThrowNotFoundException_WhenWorkspaceNotFound() {
        // Arrange
        Long nonExistingId = 2L;
        when(workspaceRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> {
            workspaceServiceImp.findWorkspaceById(nonExistingId);
        });
    }

    @Test
    void canGetAll() {
        //given
        Workspace workspace1 = new Workspace();
        Workspace workspace2 = new Workspace();
        workspace1.setId(1L);
        workspace1.setName("workspaceone");
        workspace2.setId(2L);
        workspace2.setName("workspcacetwo");
        List<Workspace> workspaceList = new ArrayList<>();
        workspaceList.add(workspace1);
        workspaceList.add(workspace2);

        //when
        when(workspaceRepository.findAll()).thenReturn(workspaceList);
        List<Workspace> workspaces = workspaceServiceImp.getAll();

        //then
        assertEquals(2, workspaces.size());

    }
    @Test
    void updateOneWorkspace_ShouldThrowNameFormatException_WhenWorkspaceNotFound() {
        // Arrange
        Long nonExistingId = 2L;
        WorkspaceCreateDto workspaceCreateDto = new WorkspaceCreateDto();
        workspaceCreateDto.setName("New Workspace Name");

        when(workspaceRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NameFormatException.class, () -> {
            workspaceServiceImp.updateOneWorkspace(nonExistingId, workspaceCreateDto);
        });
        verify(workspaceRepository, times(1)).findById(nonExistingId);
        verify(workspaceRepository, never()).save(any());

    }

}
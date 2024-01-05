package org.zero2hero.applicationservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zero2hero.applicationservice.entity.Workspace;
import org.zero2hero.applicationservice.exception.IdFormatException;
import org.zero2hero.applicationservice.exception.NotFoundException;
import org.zero2hero.applicationservice.repository.WorkspaceRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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

}
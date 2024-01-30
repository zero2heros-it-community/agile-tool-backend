package org.zero2hero.applicationservice.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.zero2hero.applicationservice.dto.BoardCreateDto;
import org.zero2hero.applicationservice.entity.Board;
import org.zero2hero.applicationservice.entity.Workspace;
import org.zero2hero.applicationservice.exception.AlreadyExistException;
import org.zero2hero.applicationservice.exception.IdFormatException;
import org.zero2hero.applicationservice.exception.NameFormatException;
import org.zero2hero.applicationservice.exception.NotFoundException;
import org.zero2hero.applicationservice.repository.BoardRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoardServiceImpTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private WorkspaceService workspaceService;

    @Mock
    private KafkaTemplate kafkaTemplate;

    @InjectMocks
    private BoardServiceImp boardServiceImp;

    @Nested
    @DisplayName("TestCreate_GivenBoardCreateDto")
    class TestCreate{

        @Test
        void create_ShouldReturnSuccessfullyBoardViewDto() {

            // Given
            BoardCreateDto boardCreateDto = new BoardCreateDto();
            boardCreateDto.setWorkSpaceId("1");
            boardCreateDto.setName("newboard");

            Workspace workspace = new Workspace();
            when(workspaceService.findWorkspaceById(eq(1L))).thenReturn(workspace);
            when(boardRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

            // When
            Board createdBoard = boardServiceImp.create(boardCreateDto);

            // Then
            verify(workspaceService, times(1)).findWorkspaceById(eq(1L));
            verify(boardRepository, times(1)).save(any(Board.class));
            verify(kafkaTemplate, times(1)).send(anyString(), anyString(), any(Board.class));

            assertNotNull(createdBoard);
            assertEquals(createdBoard.getName(),boardCreateDto.getName());
            assertEquals("newboard", createdBoard.getName());
            assertEquals(workspace, createdBoard.getWorkspace());
        }

        @Test
        void create_ShouldThrowNameFormatException_WhenBoardNameIncorrectFormat(){

            Workspace workspace = new Workspace();
            workspace.setId(1L);

            BoardCreateDto boardCreateDto = new BoardCreateDto();
            boardCreateDto.setWorkSpaceId("1");
            boardCreateDto.setName("NewBoard");

            when(workspaceService.findWorkspaceById(anyLong())).thenReturn(workspace);

            assertThrows(NameFormatException.class, () -> {
                boardServiceImp.create(boardCreateDto);
            });

            verify(workspaceService, times(1)).findWorkspaceById(anyLong());
            verify(boardRepository, never()).save(any());
            verify(kafkaTemplate, never()).send(anyString(), anyString(), any(Board.class));
        }

        @Test
        void create_ShouldThrowAlreadyExistException_WhenBoardAlreadyExistInRepository() {

            Workspace workspace = new Workspace();
            workspace.setId(1L);

            BoardCreateDto boardCreateDto = new BoardCreateDto();
            boardCreateDto.setWorkSpaceId("1");
            boardCreateDto.setName("newboard");

            when(workspaceService.findWorkspaceById(anyLong())).thenReturn(workspace);
            when(boardRepository.isBoardExistInWorkSpace(anyString(),anyLong())).thenReturn(Boolean.TRUE);

            assertThrows(AlreadyExistException.class, () -> {
                boardServiceImp.create(boardCreateDto);
            });

            verify(workspaceService, times(1)).findWorkspaceById(anyLong());
            verify(boardRepository, times(1)).isBoardExistInWorkSpace(any(),anyLong());
            verify(kafkaTemplate, never()).send(anyString(), anyString(), any(Board.class));
        }

        @Test
        void create_ShouldThrowNotFoundException_WhenWorkspaceNotExistInRepository() {

            BoardCreateDto boardCreateDto = new BoardCreateDto();
            boardCreateDto.setWorkSpaceId("1");
            boardCreateDto.setName("newboard");

            when(workspaceService.findWorkspaceById(anyLong())).thenReturn(null);

            assertThrows(NotFoundException.class, () -> {
                boardServiceImp.create(boardCreateDto);
            });

            verify(workspaceService, times(1)).findWorkspaceById(anyLong());
            verify(boardRepository, never()).save(any());
            verify(kafkaTemplate, never()).send(anyString(), anyString(), any(Board.class));
        }

        @Test
        void create_ShouldThrowIdFormatException_WhenWorkspaceIdIsInvalid(){

            BoardCreateDto boardCreateDto = new BoardCreateDto();
            boardCreateDto.setWorkSpaceId("abc");
            boardCreateDto.setName("newboard");

            assertThrows(IdFormatException.class, () -> {
                boardServiceImp.create(boardCreateDto);
            });

            verify(workspaceService, never()).findWorkspaceById(anyLong());
            verify(boardRepository, never()).save(any());
            verify(kafkaTemplate, never()).send(anyString(), anyString(), any(Board.class));
        }

    }

    @Nested
    @DisplayName("TestDelete_GivenBoardId")
    class TestDelete{

        @Test
        void delete_ShouldDeleteBoard_WhenIdIsValid() {

            String boardId = "1";
            Board existingBoard = new Board();
            when(boardRepository.findById(1L)).thenReturn(Optional.of(existingBoard));
            //when(workspaceService.isValidIdFormat(validBoardId)).thenReturn(true);

            boardServiceImp.delete(boardId);

            verify(boardRepository, times(1)).delete(existingBoard);
        }

        @Test
        void delete_ShouldThrowNotFoundException_WhenBoardNotFound() {

            String nonExistingBoardId = "999";
            when(boardRepository.findById(999L)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> {
                boardServiceImp.delete(nonExistingBoardId);
            });
        }

        @Test
        void delete_ShouldThrowIdFormatException_WhenIdIsInvalid() {

            String invalidBoardId = "invalid";

            assertThrows(IdFormatException.class, () -> {
                boardServiceImp.delete(invalidBoardId);
            });
        }
    }

}

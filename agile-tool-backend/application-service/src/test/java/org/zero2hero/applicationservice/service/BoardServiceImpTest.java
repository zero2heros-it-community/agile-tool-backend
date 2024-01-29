package org.zero2hero.applicationservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zero2hero.applicationservice.entity.Board;
import org.zero2hero.applicationservice.exception.IdFormatException;
import org.zero2hero.applicationservice.exception.NotFoundException;
import org.zero2hero.applicationservice.repository.BoardRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoardServiceImpTest {

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private BoardServiceImp boardServiceImp;

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

        when(boardServiceImp.isAValidIdFormat(invalidBoardId)).thenReturn(Boolean.FALSE);

        assertThrows(IdFormatException.class, () -> {
            boardServiceImp.delete(invalidBoardId);
        });
    }

}

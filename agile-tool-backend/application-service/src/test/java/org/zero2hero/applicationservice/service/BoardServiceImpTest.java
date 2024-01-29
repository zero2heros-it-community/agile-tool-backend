package org.zero2hero.applicationservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zero2hero.applicationservice.entity.Board;
import org.zero2hero.applicationservice.entity.Workspace;
import org.zero2hero.applicationservice.repository.BoardRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoardServiceImpTest {
    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private BoardServiceImp boardServiceImp;

    @Test
    void findByWorkspace() {
        //given
        Workspace workspace = new Workspace();
        workspace.setName("newworkspace");
        workspace.setId(1L);
        Board board1 = new Board();
        board1.setName("board1");
        board1.setId(1L);
        board1.setWorkspace(workspace);
        Board board2 = new Board();
        board2.setId(2L);
        board2.setName("boardtwo");
        board2.setWorkspace(workspace);
        List<Board> expectedList = new ArrayList<>();
        expectedList.add(board1);
        expectedList.add(board2);
        //when
        when(boardRepository.findBoardsByWorkspaceId(workspace.getId())).thenReturn(expectedList);
        //then
        assertEquals(2, expectedList.size());

    }
}
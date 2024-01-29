package org.zero2hero.applicationservice.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.zero2hero.applicationservice.entity.Board;
import org.zero2hero.applicationservice.entity.Workspace;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class BoardRepositoryTest {
    @Autowired
    private BoardRepository underTest;

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void canFindBoardsByWorkspaceId() {
       //given
        Workspace workspace = new Workspace();
        workspace.setName("newworkspace");
        workspace.setId(1L);
        workspaceRepository.save(workspace);
        Board board1 = new Board();
        board1.setName("board1");
        board1.setId(1L);
        board1.setWorkspace(workspace);
        underTest.save(board1);
        Board board2 = new Board();
        board2.setId(2L);
        board2.setName("boardtwo");
        board2.setWorkspace(workspace);
        underTest.save(board2);
        //when
        List<Board> boardList = underTest.findBoardsByWorkspaceId(workspace.getId());
        //then
        assertEquals(2,boardList.size());


    }
}
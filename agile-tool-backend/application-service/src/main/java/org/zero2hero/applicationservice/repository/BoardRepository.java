package org.zero2hero.applicationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.zero2hero.applicationservice.entity.Board;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findByName(String name);
    @Query(value = "SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Boards b where b.workspace_id = :workspaceId and b.board_name= :boardName", nativeQuery = true)
    Boolean isBoardExistInWorkSpace(@Param("boardName") String boardName, @Param("workspaceId") Long workspaceId);

    @Query(value = "SELECT * FROM boards b where b.workspace_id = :workspaceId", nativeQuery = true)
    List<Board> findBoardsByWorkspaceId(@Param("workspaceId") Long workspaceId);
    List<Board> findByWorkspaceId(Long workspaceId);
}

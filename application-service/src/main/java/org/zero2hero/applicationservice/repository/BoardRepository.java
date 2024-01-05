package org.zero2hero.applicationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.zero2hero.applicationservice.entity.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query(value = "SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Boards b where b.workspace_id = :workspaceId and b.board_name= :boardName", nativeQuery = true)
    Boolean isBoardExistInWorkSpace(@Param("boardName") String boardName, @Param("workspaceId") Long workspaceId);

}

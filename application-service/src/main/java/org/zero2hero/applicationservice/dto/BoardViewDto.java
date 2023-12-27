package org.zero2hero.applicationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zero2hero.applicationservice.entity.Board;
import org.zero2hero.applicationservice.entity.Workspace;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardViewDto {
    private String id;
    private String workSpaceId;
    private String name;

    public static BoardViewDto of(Board board) {
        return new BoardViewDto(board.getId().toString(), board.getWorkspace().getId().toString() ,board.getName());
    }
}
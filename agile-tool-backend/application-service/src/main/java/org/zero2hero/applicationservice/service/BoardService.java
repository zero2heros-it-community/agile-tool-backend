package org.zero2hero.applicationservice.service;


import org.apache.coyote.BadRequestException;
import org.zero2hero.applicationservice.dto.BoardCreateDto;
import org.zero2hero.applicationservice.dto.BoardUpdateDto;
import org.zero2hero.applicationservice.dto.BoardViewDto;
import org.zero2hero.applicationservice.entity.Board;
import org.zero2hero.applicationservice.entity.Workspace;

import java.util.List;

public interface BoardService {
    Board create(BoardCreateDto boardCreateDto);

    BoardViewDto update(String boardId, BoardUpdateDto boardUpdateDto) throws BadRequestException;

    void delete (String boardId);
    List<Board> findByWorkspace(Workspace workspace);


}
package org.zero2hero.applicationservice.service;

import org.zero2hero.applicationservice.dto.BoardCreateDto;
import org.zero2hero.applicationservice.dto.BoardUpdateDto;
import org.zero2hero.applicationservice.dto.BoardViewDto;
import org.zero2hero.applicationservice.entity.Board;

public interface BoardService {
    Board create(BoardCreateDto boardCreateDto);

    BoardViewDto update(String boardId, BoardUpdateDto boardUpdateDto) ;

    void delete (String boardId);


    Board findBoardById(Long Id);
}
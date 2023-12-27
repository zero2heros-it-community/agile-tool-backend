package org.zero2hero.applicationservice.service;


import org.zero2hero.applicationservice.dto.BoardCreateDto;
import org.zero2hero.applicationservice.dto.BoardViewDto;

public interface BoardService {
    BoardViewDto create(BoardCreateDto boardCreateDto);
}
package org.zero2hero.applicationservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zero2hero.applicationservice.dto.BoardCreateDto;
import org.zero2hero.applicationservice.dto.BoardViewDto;
import org.zero2hero.applicationservice.service.BoardService;

@RestController
@RequestMapping("/api/v1/board")
public class BoardController {

    @Autowired
    BoardService boardService;

    @PostMapping
    public ResponseEntity<BoardViewDto> createBoard(@RequestBody BoardCreateDto boardCreateDto) {
        BoardViewDto boardViewDto = boardService.create(
                boardCreateDto);
        return new ResponseEntity<>(boardViewDto, HttpStatus.CREATED);
    }
}

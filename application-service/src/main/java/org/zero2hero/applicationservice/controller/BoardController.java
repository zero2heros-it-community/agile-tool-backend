package org.zero2hero.applicationservice.controller;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zero2hero.applicationservice.dto.BoardCreateDto;
import org.zero2hero.applicationservice.dto.BoardUpdateDto;
import org.zero2hero.applicationservice.dto.BoardViewDto;
import org.zero2hero.applicationservice.service.BoardService;

@RestController
@RequestMapping("/api/v1/boards")
public class BoardController {

    @Autowired
    BoardService boardService;

    @PostMapping
    public ResponseEntity<BoardViewDto> createBoard(@RequestBody BoardCreateDto boardCreateDto) {
        BoardViewDto boardViewDto = BoardViewDto.of(boardService.create(
                boardCreateDto));
        return new ResponseEntity<>(boardViewDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardViewDto> updateBoard(@PathVariable String id, @RequestBody BoardUpdateDto boardUpdateDto) throws BadRequestException {
        BoardViewDto boardViewDto = boardService.update(id, boardUpdateDto);
        return new ResponseEntity<>(boardViewDto, HttpStatus.OK);
    }
}

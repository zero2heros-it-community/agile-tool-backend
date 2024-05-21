package org.zero2hero.applicationservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zero2hero.applicationservice.dto.BoardCreateDto;
import org.zero2hero.applicationservice.dto.BoardUpdateDto;
import org.zero2hero.applicationservice.dto.BoardViewDto;
import org.zero2hero.applicationservice.service.BoardService;

@RestController
@RequestMapping("/application/api/v1/boards")
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
    public ResponseEntity<BoardViewDto> updateBoard(@PathVariable String id, @RequestBody BoardUpdateDto boardUpdateDto){
        BoardViewDto boardViewDto = boardService.update(id, boardUpdateDto);
        return new ResponseEntity<>(boardViewDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBoard(@PathVariable String id) {
        boardService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        BoardViewDto boardViewDto= BoardViewDto.of(boardService.findBoardById(id));
        return new ResponseEntity<>(boardViewDto,HttpStatus.OK);
    }

}

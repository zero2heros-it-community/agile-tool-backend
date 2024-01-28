package org.zero2hero.applicationservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zero2hero.applicationservice.dto.BoardViewDto;
import org.zero2hero.applicationservice.dto.WorkspaceCreateDto;
import org.zero2hero.applicationservice.dto.WorkspaceViewDto;
import org.zero2hero.applicationservice.entity.Board;
import org.zero2hero.applicationservice.entity.Workspace;
import org.zero2hero.applicationservice.service.BoardService;
import org.zero2hero.applicationservice.service.WorkspaceService;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/application/api/v1/work-space")
public class WorkspaceController {

    @Autowired
    WorkspaceService workspaceService;
    @Autowired
    BoardService boardService;

    @PostMapping
    public ResponseEntity<WorkspaceViewDto> createWorkspace(@RequestBody WorkspaceCreateDto workspaceCreateDto) {
        WorkspaceViewDto workspaceViewDto = workspaceService.create(
                workspaceCreateDto);
        return new ResponseEntity<>(workspaceViewDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkspaceViewDto> getWorkspace(@PathVariable Long id) {
        Workspace workspace = workspaceService.findWorkspaceById(id);
        return new ResponseEntity<>(WorkspaceViewDto.of(workspace), HttpStatus.OK);
    }

    @GetMapping("{id}/boards")
    public ResponseEntity<?> getWorkspacesWithBoards(@PathVariable Long id) {
        Workspace workspace = workspaceService.findWorkspaceById(id);
        List<Board> boards = boardService.findByWorkspace(workspace);
        List<BoardViewDto> boardViewDtos = new ArrayList<>();
        boards.forEach(board -> {
            boardViewDtos.add(BoardViewDto.of(board));
        });
        return ResponseEntity.ok(boardViewDtos);
    }

}

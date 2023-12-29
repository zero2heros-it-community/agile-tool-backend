package org.zero2hero.applicationservice.service;

import org.apache.coyote.BadRequestException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.zero2hero.applicationservice.dto.BoardCreateDto;
import org.zero2hero.applicationservice.dto.BoardUpdateDto;
import org.zero2hero.applicationservice.dto.BoardViewDto;
import org.zero2hero.applicationservice.dto.WorkspaceViewDto;
import org.zero2hero.applicationservice.entity.Board;
import org.zero2hero.applicationservice.entity.Workspace;
import org.zero2hero.applicationservice.exception.AlreadyExistException;
import org.zero2hero.applicationservice.exception.ForbiddenException;
import org.zero2hero.applicationservice.exception.IncorrectFormatException;
import org.zero2hero.applicationservice.exception.NotFoundException;
import org.zero2hero.applicationservice.repository.BoardRepository;

@Service
public class BoardServiceImp implements BoardService {
    private final KafkaTemplate kafkaTemplate;
    private final BoardRepository boardRepository;
    private final WorkspaceService workspaceService;

    public BoardServiceImp(KafkaTemplate kafkaTemplate,
                           BoardRepository boardRepository,
                           WorkspaceService workspaceService) {
        this.kafkaTemplate = kafkaTemplate;
        this.boardRepository = boardRepository;
        this.workspaceService = workspaceService;
    }

    @Override
    public BoardViewDto create(BoardCreateDto boardCreateDto) {
        Board board = new Board();
        Workspace workspace = workspaceService.findWorkspaceById(Long.valueOf(boardCreateDto.getWorkSpaceId()));

        if (workspace==null)
            throw new NotFoundException("workspace not found");
        if (isBoardExist(boardCreateDto.getName(), Long.valueOf(boardCreateDto.getWorkSpaceId())))
            throw new AlreadyExistException("board is already exist");
        if (!isAValidBoardName(boardCreateDto.getName()))
            throw new IncorrectFormatException("workspace Id or board name is in incorrect format");

        board.setName(boardCreateDto.getName());
        board.setWorkspace(workspace);

        board = boardRepository.save(board);
        this.kafkaTemplate.send("first_topic", "user-key", board);
        return BoardViewDto.of(board);
    }

    @Override
    public BoardViewDto update(String id, BoardUpdateDto boardUpdateDto) throws BadRequestException {
        validateUpdateBoardRequest(id, boardUpdateDto);

        Board board = boardRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new NotFoundException("Board not found"));

        if (boardRepository.existsByWorkSpaceIdAndName(board.getWorkspace().getId(), boardUpdateDto.getName())) {
            throw new ForbiddenException("Board is already exist");
        }

        board.setName(boardUpdateDto.getName());
        return BoardViewDto.of(boardRepository.save(board));
    }

    private void validateUpdateBoardRequest(String id, BoardUpdateDto request) throws BadRequestException {
        if (!isValidFormat(id, request.getName())) {
            throw new BadRequestException("Board ID or name is in incorrect format");
        }
    }

    private boolean isValidFormat(String id, String name) {
        // Implement your format validation logic
        // This is just a placeholder method
        return id != null && name != null && isAValidBoardName(name);
    }

    private boolean isBoardExist(String boardName, Long workspaceId) {
        return boardRepository.isBoardExistInWorkSpace(boardName, workspaceId);
    }

    private boolean isAValidBoardName(String boardName) {

        return boardName.matches("^[a-z]+$");
    }
}

package org.zero2hero.applicationservice.service;

import org.apache.coyote.BadRequestException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.zero2hero.applicationservice.dto.BoardCreateDto;
import org.zero2hero.applicationservice.dto.BoardUpdateDto;
import org.zero2hero.applicationservice.dto.BoardViewDto;
import org.zero2hero.applicationservice.entity.Board;
import org.zero2hero.applicationservice.entity.Workspace;
import org.zero2hero.applicationservice.exception.AlreadyExistException;
import org.zero2hero.applicationservice.exception.NameFormatException;
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
    public Board create(BoardCreateDto boardCreateDto) {
        Board board = new Board();
        Workspace workspace = workspaceService.findWorkspaceById(Long.valueOf(boardCreateDto.getWorkSpaceId()));

        if (workspace==null)
            throw new NotFoundException("workspace not found");
        if (isBoardExist(boardCreateDto.getName(), Long.valueOf(boardCreateDto.getWorkSpaceId())))
            throw new AlreadyExistException("board is already exist");
        if (!isAValidBoardName(boardCreateDto.getName()))
          throw new NameFormatException("Board name is in incorrect format");

        board.setName(boardCreateDto.getName());
        board.setWorkspace(workspace);

        board = boardRepository.save(board);
        this.kafkaTemplate.send("first_topic", "user-key", board);
        return board;
    }

    @Override
    public BoardViewDto update(String id, BoardUpdateDto boardUpdateDto) throws BadRequestException {

        if (!isAValidBoardName(boardUpdateDto.getName()))
            throw new BadRequestException("Board ID or name is in incorrect format");

        if (isBoardExist(boardUpdateDto.getName(),Long.valueOf(boardUpdateDto.getWorkSpaceId()))) {
            throw new AlreadyExistException("Board is already exist");
        }

        Board board = boardRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new NotFoundException("Board not found"));

        board.setName(boardUpdateDto.getName());
        return BoardViewDto.of(boardRepository.save(board));
    }

    private boolean isBoardExist(String boardName, Long workspaceId) {
        return boardRepository.isBoardExistInWorkSpace(boardName, workspaceId);
    }

    private boolean isAValidBoardName(String boardName) {

        return boardName.matches("^[a-z]+$");
    }
}

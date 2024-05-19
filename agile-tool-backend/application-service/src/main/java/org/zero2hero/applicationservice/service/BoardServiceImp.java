package org.zero2hero.applicationservice.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.zero2hero.applicationservice.dto.BoardCreateDto;
import org.zero2hero.applicationservice.dto.BoardUpdateDto;
import org.zero2hero.applicationservice.dto.BoardViewDto;
import org.zero2hero.applicationservice.entity.Board;
import org.zero2hero.applicationservice.entity.Workspace;
import org.zero2hero.applicationservice.exception.*;
import org.zero2hero.applicationservice.repository.BoardRepository;
import org.zero2hero.applicationservice.util.LoggedUsername;

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

        if (!isAValidIdFormat(boardCreateDto.getWorkSpaceId()))
            throw new IdFormatException("Workspace Id is in incorrect format");

        Workspace workspace = workspaceService.findWorkspaceById(Long.valueOf(boardCreateDto.getWorkSpaceId()));

        if (workspace == null)
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
    public BoardViewDto update(String id, BoardUpdateDto boardUpdateDto) {


        if (isBoardExist(boardUpdateDto.getName(), Long.valueOf(boardUpdateDto.getWorkSpaceId()))) {
            throw new AlreadyExistException("Board is already exist");
        }

        Board board = boardRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new NotFoundException("Board not found"));

        board.setName(boardUpdateDto.getName());
        return BoardViewDto.of(boardRepository.save(board));
    }

    @Override
    public void delete(String boardId) {

        if (!isAValidIdFormat(boardId))
            throw new IdFormatException("Board Id is in incorrect format");

        Board board = boardRepository.findById(Long.valueOf(boardId)).orElseThrow(() -> new NotFoundException("Board not found"));

        boardRepository.delete(board);
    }

    @Override
    public Board findBoardById(Long Id) {
        Board board = boardRepository.findById(Id).orElseThrow(
                () -> new NotFoundException("Board Not found"));
        String username = LoggedUsername.getUsernameFromAuthentication();
        if (!board.getWorkspace().getUsername().equals(username)) {
            throw new BelongsToAnotherUserException(" Board belongs to another user");
        }

        return board;
    }

    boolean isBoardExist(String boardName, Long workspaceId) {
        return boardRepository.isBoardExistInWorkSpace(boardName, workspaceId);
    }

    boolean isAValidBoardName(String boardName) {

        return boardName.matches("^[a-z]+$");
    }

    boolean isAValidIdFormat(String boardId) {

        return boardId.matches("\\d+");
    }
}

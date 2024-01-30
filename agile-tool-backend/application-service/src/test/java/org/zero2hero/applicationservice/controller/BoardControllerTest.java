package org.zero2hero.applicationservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.zero2hero.applicationservice.dto.BoardCreateDto;
import org.zero2hero.applicationservice.entity.Board;
import org.zero2hero.applicationservice.entity.Workspace;
import org.zero2hero.applicationservice.exception.*;
import org.zero2hero.applicationservice.service.BoardService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = BoardCreateDto.class)
public class BoardControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private BoardService boardService;

    @InjectMocks
    private BoardController boardController;

    @Test
    public void delete_canDeleteBoard() throws Exception {
        // given
        String boardId = "1";

        // when
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(boardController).build();

        // then
        mockMvc.perform(delete("/application/api/v1/boards/" + boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boardId)))
                .andExpect(status().isOk());

    }

    @Test
    public void delete_shouldThrowErrorWhenInvalidIdFormat() throws Exception {
        String boardId = "dsds";
        //when
        doThrow(new IdFormatException("Id format exception"))
                .when(boardService)
                .delete(anyString());

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(boardController)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();

        // then
        mockMvc.perform(delete("/application/api/v1/boards/" + boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boardId)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Id format exception"))
                .andExpect(jsonPath("$.code").value(400));


    }

    @Test
    public void delete_shouldThrowErrorWhenBoardNotFound() throws Exception {
        String boardId = "1";
        //when
        doThrow(new NotFoundException("Not found exception"))
                .when(boardService)
                .delete(anyString());

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(boardController)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();

        // then
        mockMvc.perform(delete("/application/api/v1/boards/" + boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boardId)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Not found exception"))
                .andExpect(jsonPath("$.code").value(404));


    }

    @Test
    public void create_canCreateBoard() throws Exception {
        // given
        Workspace mockWorkspace = new Workspace();
        mockWorkspace.setId(3L);
        BoardCreateDto boardCreateDto = new BoardCreateDto();
        Board mockBoard = new Board();
        mockBoard.setId(1L);
        mockBoard.setName("newBoard");
        mockBoard.setWorkspace(mockWorkspace);

        // when
        when(boardService.create(any(BoardCreateDto.class))).thenReturn(mockBoard);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(boardController).build();

        // then
        mockMvc.perform(post("/application/api/v1/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boardCreateDto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void create_shouldThrowErrorWhenInvalidWorkspaceId() throws Exception {
        String workspaceId = "dsds";
        BoardCreateDto boardCreateDto = new BoardCreateDto("newboard",workspaceId);

        //when
        doThrow(new IdFormatException("Id format exception"))
                .when(boardService)
                .create(boardCreateDto);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(boardController)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();

        // then
        mockMvc.perform(post("/application/api/v1/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boardCreateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Id format exception"))
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    public void create_shouldThrowErrorWhenBoardNotFound() throws Exception {

        String workspaceId = "1";
        BoardCreateDto boardCreateDto = new BoardCreateDto("newboard",workspaceId);

        //when
        doThrow(new NotFoundException("Not found exception"))
                .when(boardService)
                .create(any(BoardCreateDto.class));

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(boardController)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();

        // then
        mockMvc.perform(post("/application/api/v1/boards/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boardCreateDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Not found exception"))
                .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    public void create_shouldThrowErrorWhenBoardAlreadyExist() throws Exception {

        String workspaceId = "1";
        BoardCreateDto boardCreateDto = new BoardCreateDto("newboard",workspaceId);

        //when
        doThrow(new AlreadyExistException("Already Exist exception"))
                .when(boardService)
                .create(any(BoardCreateDto.class));

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(boardController)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();

        // then
        mockMvc.perform(post("/application/api/v1/boards/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boardCreateDto)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Already Exist exception"))
                .andExpect(jsonPath("$.code").value(403));
    }

    @Test
    public void create_shouldThrowErrorWhenInvalidBoardName() throws Exception {

        String workspaceId = "1";
        BoardCreateDto boardCreateDto = new BoardCreateDto("NewBoard",workspaceId);

        //when
        doThrow(new NameFormatException("Name Format Exception"))
                .when(boardService)
                .create(any(BoardCreateDto.class));

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(boardController)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();

        // then
        mockMvc.perform(post("/application/api/v1/boards/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boardCreateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Name Format Exception"))
                .andExpect(jsonPath("$.code").value(400));
    }

}

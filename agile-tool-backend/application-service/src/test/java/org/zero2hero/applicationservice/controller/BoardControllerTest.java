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
import org.zero2hero.applicationservice.exception.CustomExceptionHandler;
import org.zero2hero.applicationservice.exception.IdFormatException;
import org.zero2hero.applicationservice.exception.NotFoundException;
import org.zero2hero.applicationservice.service.BoardService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
    public void canDeleteBoard() throws Exception {
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
    public void shouldThrowErrorWhenInvalidIdFormat() throws Exception {
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
    public void shouldThrowErrorWhenBoardNotFound() throws Exception {
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
}

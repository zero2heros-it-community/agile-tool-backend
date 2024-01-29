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
import org.zero2hero.applicationservice.dto.WorkspaceCreateDto;
import org.zero2hero.applicationservice.dto.WorkspaceViewDto;
import org.zero2hero.applicationservice.entity.Board;
import org.zero2hero.applicationservice.entity.Workspace;
import org.zero2hero.applicationservice.exception.CustomExceptionHandler;
import org.zero2hero.applicationservice.exception.NameFormatException;
import org.zero2hero.applicationservice.service.BoardService;
import org.zero2hero.applicationservice.service.WorkspaceService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest
@ContextConfiguration(classes = WorkspaceCreateDto.class)
class WorkspaceControllerTest {



    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private WorkspaceService workspaceService;

    @Mock
    private BoardService boardService;

    @InjectMocks
    private WorkspaceController workspaceController;




    @Test
    public void canCreateWorkspace() throws Exception {
        // given
        WorkspaceCreateDto workspaceCreateDto = new WorkspaceCreateDto();
        WorkspaceViewDto mockWorkspaceViewDto = new WorkspaceViewDto();
        mockWorkspaceViewDto.setId("1");
        mockWorkspaceViewDto.setName("newworkspace");

        // when
        when(workspaceService.create(any(WorkspaceCreateDto.class))).thenReturn(mockWorkspaceViewDto);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(workspaceController).build();

        // then
        mockMvc.perform(post("/application/api/v1/work-space")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workspaceCreateDto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldThrowError() throws Exception {
        // given
        WorkspaceCreateDto workspaceCreateDto = new WorkspaceCreateDto();

        //when
        when(workspaceService.create(any(WorkspaceCreateDto.class)))
                .thenThrow(new NameFormatException("Test1"));

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(workspaceController)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();

        // then
        mockMvc.perform(post("/application/api/v1/work-space")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workspaceCreateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Test1"))
                .andExpect(jsonPath("$.code").value(400));


    }

    @Test
    public void canGetWorkspace() throws Exception {
        // given
        Long workspaceId = 1L;
        Workspace workspace = new Workspace(); // Varsayılan değerlerle dolu bir workspace nesnesi
        workspace.setId(workspaceId);
        workspace.setName("testWorkspace");

        WorkspaceViewDto workspaceViewDto = WorkspaceViewDto.of(workspace);

        // when
        when(workspaceService.findWorkspaceById(workspaceId)).thenReturn(workspace);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(workspaceController)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();

        // then
        mockMvc.perform(get("/application/api/v1/work-space/" + workspaceId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(workspace.getId().toString()))
                .andExpect(jsonPath("$.name").value(workspace.getName()));
    }

    @Test
    public void testGetWorkspacesWithBoards() throws Exception {
        // Mock data
        Long workspaceId = 1L;
        Workspace workspace = new Workspace();
        workspace.setId(workspaceId);

        List<Board> boards = new ArrayList<>();
        Board board1 = new Board();
        board1.setId(1L);
        board1.setWorkspace(workspace);
        board1.setName("newboard");
        boards.add(board1);

        // Mock behavior
        when(workspaceService.findWorkspaceById(workspaceId)).thenReturn(workspace);
        when(boardService.findByWorkspace(workspace)).thenReturn(boards);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(workspaceController)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();
        // Perform the GET request
        mockMvc.perform(get("/application/api/v1/work-space/" + workspaceId + "/boards").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1)) // Assuming one board in the list
                .andExpect(jsonPath("$[0].id").value(1));  // Assuming the ID of the first board is 1


    }
}
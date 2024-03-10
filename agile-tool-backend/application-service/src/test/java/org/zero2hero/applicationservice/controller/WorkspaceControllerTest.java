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
import org.zero2hero.applicationservice.entity.Workspace;
import org.zero2hero.applicationservice.exception.CustomExceptionHandler;
import org.zero2hero.applicationservice.exception.NameFormatException;
import org.zero2hero.applicationservice.service.WorkspaceService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = WorkspaceCreateDto.class)
class WorkspaceControllerTest {



    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private WorkspaceService workspaceService;

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
        mockMvc.perform(post("/api/v1/work-space")
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
        mockMvc.perform(post("/api/v1/work-space")
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
        mockMvc.perform(get("/api/v1/work-space/" + workspaceId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(workspace.getId().toString()))
                .andExpect(jsonPath("$.name").value(workspace.getName()));
    }

    @Test
    public void canDeleteWorkspace() throws Exception{
        //  Given
        Long workspaceId=1L;
        Workspace workspace = new Workspace();
        workspace.setId(workspaceId);
        workspace.setName("testWorkspace");

        //When
        doNothing().when(workspaceService).deleteWorkspaceById(workspaceId);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(workspaceController)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();

        //Then
        mockMvc.perform(delete("/application/api/v1/work-space/" + workspaceId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Workspace is successfully deleted']").value(true));
    }



    @Test
    public void canGetAll() throws Exception {
        // given
        Workspace workspace1 = new Workspace();
        workspace1.setId(1L);
        workspace1.setName("workspaceone");

        Workspace workspace2 = new Workspace();
        workspace2.setId(2L);
        workspace2.setName("workspacetwo");

        List<Workspace> mockWorkspaceList = Arrays.asList(workspace1, workspace2);

        // when
        when(workspaceService.getAll()).thenReturn(mockWorkspaceList);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(workspaceController)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();

         //then
        mockMvc.perform(get("/application/api/v1/work-space")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(mockWorkspaceList.size()))
                .andExpect(jsonPath("$[0].id").value(workspace1.getId()))
                .andExpect(jsonPath("$[0].name").value(workspace1.getName()))
                .andExpect(jsonPath("$[1].id").value(workspace2.getId()))
                .andExpect(jsonPath("$[1].name").value(workspace2.getName()));

    }
}
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
import org.zero2hero.applicationservice.exception.CustomExceptionHandler;
import org.zero2hero.applicationservice.exception.NameFormatException;
import org.zero2hero.applicationservice.service.WorkspaceService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/v1/work-space")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workspaceCreateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Test1"))
               .andExpect(jsonPath("$.code").value(400));
    }
}
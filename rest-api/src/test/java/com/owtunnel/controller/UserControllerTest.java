package com.owtunnel.controller;

import com.owtunnel.dto.request.UserRequest;
import com.owtunnel.dto.response.UserResponse;
import com.owtunnel.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = com.owtunnel.security.JwtAuthenticationFilter.class)
})
@AutoConfigureMockMvc(addFilters = false)
@Import(UserControllerTest.TestConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserRequest sampleRequest;
    private UserResponse sampleResponse;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public UserService userService() {
            return Mockito.mock(UserService.class);
        }
    }

    @BeforeEach
    void setUp() {
        sampleRequest = new UserRequest();
        sampleRequest.setEmail("test@example.com");
        sampleRequest.setPassword("password123");

        sampleResponse = new UserResponse();
        sampleResponse.setId(1L);
        sampleResponse.setEmail("test@example.com");
        sampleResponse.setCreatedAt(LocalDateTime.now());
        sampleResponse.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void getAll_shouldReturnList() throws Exception {
        Mockito.when(userService.getAll()).thenReturn(List.of(sampleResponse));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getById_shouldReturnItem() throws Exception {
        Mockito.when(userService.getById(1L)).thenReturn(sampleResponse);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void create_shouldReturnCreated() throws Exception {
        Mockito.when(userService.create(any(UserRequest.class))).thenReturn(sampleResponse);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void update_shouldReturnUpdated() throws Exception {
        Mockito.when(userService.update(eq(1L), any(UserRequest.class))).thenReturn(sampleResponse);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void delete_shouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(userService).delete(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }
}

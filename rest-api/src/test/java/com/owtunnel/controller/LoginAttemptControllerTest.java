package com.owtunnel.controller;

import com.owtunnel.dto.request.LoginAttemptRequest;
import com.owtunnel.dto.response.LoginAttemptResponse;
import com.owtunnel.service.LoginAttemptService;
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

@WebMvcTest(controllers = LoginAttemptController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = com.owtunnel.security.JwtAuthenticationFilter.class)
})
@AutoConfigureMockMvc(addFilters = false)
@Import(LoginAttemptControllerTest.TestConfig.class)
class LoginAttemptControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private LoginAttemptService loginAttemptService;
    @Autowired private ObjectMapper objectMapper;

    private LoginAttemptRequest sampleRequest;
    private LoginAttemptResponse sampleResponse;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public LoginAttemptService loginAttemptService() {
            return Mockito.mock(LoginAttemptService.class);
        }
    }

    @BeforeEach
    void setUp() {
        sampleRequest = new LoginAttemptRequest();
        sampleRequest.setIpAddress("192.168.1.1");
        sampleRequest.setAttemptCount(3);
        sampleRequest.setBlockedUntil("2025-01-01T12:00:00");

        sampleResponse = new LoginAttemptResponse();
        sampleResponse.setId(1L);
        sampleResponse.setIpAddress("192.168.1.1");
        sampleResponse.setAttemptCount(3);
        sampleResponse.setBlockedUntil("2025-01-01T12:00:00");
        sampleResponse.setCreatedAt(LocalDateTime.now());
        sampleResponse.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void getAll_shouldReturnList() throws Exception {
        Mockito.when(loginAttemptService.getAll()).thenReturn(List.of(sampleResponse));

        mockMvc.perform(get("/login-attempts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getById_shouldReturnItem() throws Exception {
        Mockito.when(loginAttemptService.getById(1L)).thenReturn(sampleResponse);

        mockMvc.perform(get("/login-attempts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void create_shouldReturnCreated() throws Exception {
        Mockito.when(loginAttemptService.create(any(LoginAttemptRequest.class))).thenReturn(sampleResponse);

        mockMvc.perform(post("/login-attempts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ip_address").value("192.168.1.1"))
                .andExpect(jsonPath("$.attempt_count").value(3));
    }

    @Test
    void update_shouldReturnUpdated() throws Exception {
        Mockito.when(loginAttemptService.update(eq(1L), any(LoginAttemptRequest.class))).thenReturn(sampleResponse);

        mockMvc.perform(put("/login-attempts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.attempt_count").value(3));
    }

    @Test
    void delete_shouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(loginAttemptService).delete(1L);

        mockMvc.perform(delete("/login-attempts/1"))
                .andExpect(status().isNoContent());
    }
}

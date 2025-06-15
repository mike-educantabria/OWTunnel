package com.owtunnel.controller;

import com.owtunnel.dto.request.PasswordResetRequest;
import com.owtunnel.dto.response.PasswordResetResponse;
import com.owtunnel.service.PasswordResetService;
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

@WebMvcTest(controllers = PasswordResetController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = com.owtunnel.security.JwtAuthenticationFilter.class)
})
@AutoConfigureMockMvc(addFilters = false)
@Import(PasswordResetControllerTest.TestConfig.class)
class PasswordResetControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private PasswordResetService passwordResetService;
    @Autowired private ObjectMapper objectMapper;

    private PasswordResetRequest sampleRequest;
    private PasswordResetResponse sampleResponse;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public PasswordResetService passwordResetService() {
            return Mockito.mock(PasswordResetService.class);
        }
    }

    @BeforeEach
    void setUp() {
        sampleRequest = new PasswordResetRequest();
        sampleRequest.setUserId(1L);

        sampleResponse = new PasswordResetResponse();
        sampleResponse.setId(1L);
        sampleResponse.setUserId(1L);
        sampleResponse.setResetToken("abc123xyz");
        sampleResponse.setCreatedAt(LocalDateTime.now());
        sampleResponse.setExpiresAt(LocalDateTime.now().plusHours(1));
    }

    @Test
    void getAll_shouldReturnList() throws Exception {
        Mockito.when(passwordResetService.getAll()).thenReturn(List.of(sampleResponse));

        mockMvc.perform(get("/password-reset"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getById_shouldReturnItem() throws Exception {
        Mockito.when(passwordResetService.getById(1L)).thenReturn(sampleResponse);

        mockMvc.perform(get("/password-reset/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void create_shouldReturnCreated() throws Exception {
        Mockito.when(passwordResetService.create(any(PasswordResetRequest.class))).thenReturn(sampleResponse);

        mockMvc.perform(post("/password-reset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user_id").value(1))
                .andExpect(jsonPath("$.reset_token").value("abc123xyz"));
    }

    @Test
    void update_shouldReturnUpdated() throws Exception {
        Mockito.when(passwordResetService.update(eq(1L), any(PasswordResetRequest.class))).thenReturn(sampleResponse);

        mockMvc.perform(put("/password-reset/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value(1));
    }

    @Test
    void delete_shouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(passwordResetService).delete(1L);

        mockMvc.perform(delete("/password-reset/1"))
                .andExpect(status().isNoContent());
    }
}

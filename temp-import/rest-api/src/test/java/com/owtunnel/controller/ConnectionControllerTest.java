package com.owtunnel.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.owtunnel.dto.request.ConnectionRequest;
import com.owtunnel.dto.response.ConnectionResponse;
import com.owtunnel.service.ConnectionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ConnectionController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = com.owtunnel.security.JwtAuthenticationFilter.class)
})
@AutoConfigureMockMvc(addFilters = false)
@Import(ConnectionControllerTest.TestConfig.class)
class ConnectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ConnectionService connectionService;

    @Autowired
    private ObjectMapper objectMapper;

    private ConnectionResponse sampleResponse;
    private ConnectionRequest sampleRequest;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ConnectionService connectionService() {
            return Mockito.mock(ConnectionService.class);
        }
    }

    @BeforeEach
    void setUp() {
        sampleRequest = new ConnectionRequest();
        sampleRequest.setVpnServerId(1L);
        sampleRequest.setUserId(42L);
        sampleRequest.setDeviceInfo("Windows 11");

        sampleResponse = new ConnectionResponse();
        sampleResponse.setId(1L);
        sampleResponse.setVpnServerId(1L);
        sampleResponse.setUserId(42L);
        sampleResponse.setDeviceInfo("Windows 11");
    }

    @Test
    void getAll_shouldReturnListOfConnections() throws Exception {
        Mockito.when(connectionService.getAll()).thenReturn(List.of(sampleResponse));

        mockMvc.perform(get("/connections"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getById_shouldReturnConnection() throws Exception {
        Mockito.when(connectionService.getById(1L)).thenReturn(sampleResponse);

        mockMvc.perform(get("/connections/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void create_shouldReturnCreatedConnection() throws Exception {
        Mockito.when(connectionService.create(any(ConnectionRequest.class))).thenReturn(sampleResponse);

        mockMvc.perform(post("/connections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void update_shouldReturnUpdatedConnection() throws Exception {
        Mockito.when(connectionService.update(eq(1L), any(ConnectionRequest.class))).thenReturn(sampleResponse);

        mockMvc.perform(put("/connections/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void delete_shouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(connectionService).delete(1L);

        mockMvc.perform(delete("/connections/1"))
                .andExpect(status().isNoContent());
    }
}

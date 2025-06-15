package com.owtunnel.controller;

import com.owtunnel.dto.request.VpnServerRequest;
import com.owtunnel.dto.response.VpnServerResponse;
import com.owtunnel.service.VpnServerService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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

@WebMvcTest(controllers = VpnServerController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = com.owtunnel.security.JwtAuthenticationFilter.class)
})
@AutoConfigureMockMvc(addFilters = false)
@Import(VpnServerControllerTest.TestConfig.class)
class VpnServerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VpnServerService vpnServerService;

    @Autowired
    private ObjectMapper objectMapper;

    private VpnServerRequest sampleRequest;
    private VpnServerResponse sampleResponse;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public VpnServerService vpnServerService() {
            return Mockito.mock(VpnServerService.class);
        }
    }

    @BeforeEach
    void setUp() {
        sampleRequest = new VpnServerRequest();
        sampleRequest.setCountry("Test Country");
        sampleRequest.setCity("Test Server");
        sampleRequest.setHostname("vpn.test.com");
        sampleRequest.setIpAddress("192.168.1.1");
        sampleRequest.setConfigFileUrl("https://test.com/config.ovpn");
        sampleRequest.setIsFree(false);
        sampleRequest.setIsActive(true);

        sampleResponse = new VpnServerResponse();
        sampleResponse.setId(1L);
        sampleResponse.setCountry("Test Country");
        sampleResponse.setCity("Test Server");
        sampleResponse.setHostname("vpn.test.com");
        sampleResponse.setIpAddress("192.168.1.1");
        sampleResponse.setConfigFileUrl("https://test.com/config.ovpn");
        sampleResponse.setIsFree(false);
        sampleResponse.setIsActive(true);
        sampleResponse.setCreatedAt(java.time.LocalDateTime.now());
        sampleResponse.setUpdatedAt(java.time.LocalDateTime.now());
    }

    @Test
    void getAll_shouldReturnList() throws Exception {
        Mockito.when(vpnServerService.getAll()).thenReturn(List.of(sampleResponse));

        mockMvc.perform(get("/vpn-servers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getById_shouldReturnItem() throws Exception {
        Mockito.when(vpnServerService.getById(1L)).thenReturn(sampleResponse);

        mockMvc.perform(get("/vpn-servers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void create_shouldReturnCreated() throws Exception {
        Mockito.when(vpnServerService.create(any(VpnServerRequest.class))).thenReturn(sampleResponse);

        mockMvc.perform(post("/vpn-servers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.city").value("Test Server"));
    }

    @Test
    void update_shouldReturnUpdated() throws Exception {
        Mockito.when(vpnServerService.update(eq(1L), any(VpnServerRequest.class))).thenReturn(sampleResponse);

        mockMvc.perform(put("/vpn-servers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ip_address").value("192.168.1.1"));
    }

    @Test
    void delete_shouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(vpnServerService).delete(1L);

        mockMvc.perform(delete("/vpn-servers/1"))
                .andExpect(status().isNoContent());
    }
}

package com.owtunnel.controller;

import com.owtunnel.dto.request.SubscriptionRequest;
import com.owtunnel.dto.response.SubscriptionResponse;
import com.owtunnel.service.SubscriptionService;
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

@WebMvcTest(controllers = SubscriptionController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = com.owtunnel.security.JwtAuthenticationFilter.class)
})
@AutoConfigureMockMvc(addFilters = false)
@Import(SubscriptionControllerTest.TestConfig.class)
class SubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private ObjectMapper objectMapper;

    private SubscriptionRequest sampleRequest;
    private SubscriptionResponse sampleResponse;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public SubscriptionService subscriptionService() {
            return Mockito.mock(SubscriptionService.class);
        }
    }

    @BeforeEach
    void setUp() {
        sampleRequest = new SubscriptionRequest();
        sampleRequest.setUserId(1L);
        sampleRequest.setPlanId(2L);
        sampleRequest.setAutoRenew(true);

        sampleResponse = new SubscriptionResponse();
        sampleResponse.setId(1L);
        sampleResponse.setUserId(1L);
        sampleResponse.setPlanId(2L);
        sampleResponse.setCreatedAt(LocalDateTime.now());
        sampleResponse.setUpdatedAt(LocalDateTime.now());
        sampleResponse.setAutoRenew(true);
    }

    @Test
    void getAll_shouldReturnList() throws Exception {
        Mockito.when(subscriptionService.getAll()).thenReturn(List.of(sampleResponse));

        mockMvc.perform(get("/subscriptions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getById_shouldReturnItem() throws Exception {
        Mockito.when(subscriptionService.getById(1L)).thenReturn(sampleResponse);

        mockMvc.perform(get("/subscriptions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void create_shouldReturnCreated() throws Exception {
        Mockito.when(subscriptionService.create(any(SubscriptionRequest.class))).thenReturn(sampleResponse);

        mockMvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1));
    }

    @Test
    void update_shouldReturnUpdated() throws Exception {
        Mockito.when(subscriptionService.update(eq(1L), any(SubscriptionRequest.class))).thenReturn(sampleResponse);

        mockMvc.perform(put("/subscriptions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.planId").value(2));
    }

    @Test
    void delete_shouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(subscriptionService).delete(1L);

        mockMvc.perform(delete("/subscriptions/1"))
                .andExpect(status().isNoContent());
    }
}

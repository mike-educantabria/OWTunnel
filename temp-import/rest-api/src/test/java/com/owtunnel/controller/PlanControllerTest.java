package com.owtunnel.controller;

import com.owtunnel.dto.request.PlanRequest;
import com.owtunnel.dto.response.PlanResponse;
import com.owtunnel.service.PlanService;
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

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PlanController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = com.owtunnel.security.JwtAuthenticationFilter.class)
})
@AutoConfigureMockMvc(addFilters = false)
@Import(PlanControllerTest.TestConfig.class)
class PlanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlanService planService;

    @Autowired
    private ObjectMapper objectMapper;

    private PlanRequest sampleRequest;
    private PlanResponse sampleResponse;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public PlanService planService() {
            return Mockito.mock(PlanService.class);
        }
    }

    @BeforeEach
    void setUp() {
        sampleRequest = new PlanRequest();
        sampleRequest.setName("Premium");
        sampleRequest.setPrice(BigDecimal.valueOf(9.99));
        sampleRequest.setDurationDays(30);
        sampleRequest.setIsActive(true);

        sampleResponse = new PlanResponse();
        sampleResponse.setId(1L);
        sampleResponse.setName("Premium");
        sampleResponse.setPrice(BigDecimal.valueOf(9.99));
        sampleResponse.setDurationDays(30);
        sampleResponse.setIsActive(true);
    }

    @Test
    void getAll_shouldReturnList() throws Exception {
        Mockito.when(planService.getAll()).thenReturn(List.of(sampleResponse));

        mockMvc.perform(get("/plans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getById_shouldReturnItem() throws Exception {
        Mockito.when(planService.getById(1L)).thenReturn(sampleResponse);

        mockMvc.perform(get("/plans/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void create_shouldReturnCreated() throws Exception {
        Mockito.when(planService.create(any(PlanRequest.class))).thenReturn(sampleResponse);

        mockMvc.perform(post("/plans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Premium"));
    }

    @Test
    void update_shouldReturnUpdated() throws Exception {
        Mockito.when(planService.update(eq(1L), any(PlanRequest.class))).thenReturn(sampleResponse);

        mockMvc.perform(put("/plans/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.durationDays").value(30));
    }

    @Test
    void delete_shouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(planService).delete(1L);

        mockMvc.perform(delete("/plans/1"))
                .andExpect(status().isNoContent());
    }
}


package com.owtunnel.controller;

import com.owtunnel.dto.request.PaymentRequest;
import com.owtunnel.dto.response.PaymentResponse;
import com.owtunnel.service.PaymentService;
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
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PaymentController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = com.owtunnel.security.JwtAuthenticationFilter.class)
})
@AutoConfigureMockMvc(addFilters = false)
@Import(PaymentControllerTest.TestConfig.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    private PaymentRequest sampleRequest;
    private PaymentResponse sampleResponse;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public PaymentService paymentService() {
            return Mockito.mock(PaymentService.class);
        }
    }

    @BeforeEach
    void setUp() {
        sampleRequest = new PaymentRequest();
        sampleRequest.setUserId(1L);
        sampleRequest.setPlanId(1L);
        sampleRequest.setSubscriptionId(100L);
        sampleRequest.setAmount(BigDecimal.valueOf(29.99));
        sampleRequest.setCurrency("USD");
        sampleRequest.setMethod("PAYPAL");
        sampleRequest.setStatus("PAID");
        sampleRequest.setTransactionReference("txn_123456789");

        sampleResponse = new PaymentResponse();
        sampleResponse.setId(1L);
        sampleResponse.setUserId(1L);
        sampleResponse.setPlanId(1L);
        sampleResponse.setSubscriptionId(100L);
        sampleResponse.setAmount(BigDecimal.valueOf(29.99));
        sampleResponse.setCurrency("USD");
        sampleResponse.setMethod("PAYPAL");
        sampleResponse.setStatus("PAID");
        sampleResponse.setTransactionReference("txn_123456789");
        sampleResponse.setCreatedAt(LocalDateTime.now());
        sampleResponse.setUpdatedAt(LocalDateTime.now());
    }


    @Test
    void getAll_shouldReturnList() throws Exception {
        Mockito.when(paymentService.getAll()).thenReturn(List.of(sampleResponse));

        mockMvc.perform(get("/payments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getById_shouldReturnItem() throws Exception {
        Mockito.when(paymentService.getById(1L)).thenReturn(sampleResponse);

        mockMvc.perform(get("/payments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void create_shouldReturnCreated() throws Exception {
        Mockito.when(paymentService.create(any(PaymentRequest.class))).thenReturn(sampleResponse);

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(29.99));
    }

    @Test
    void delete_shouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(paymentService).delete(1L);

        mockMvc.perform(delete("/payments/1"))
                .andExpect(status().isNoContent());
    }
}

package com.owtunnel.service;

import com.owtunnel.dto.request.PaymentRequest;
import com.owtunnel.dto.response.PaymentResponse;
import com.owtunnel.exception.EntityNotFoundException;
import com.owtunnel.mapper.PaymentMapper;
import com.owtunnel.model.entity.Payment;
import com.owtunnel.model.entity.Plan;
import com.owtunnel.model.entity.Subscription;
import com.owtunnel.model.entity.User;
import com.owtunnel.repository.PaymentRepository;
import com.owtunnel.repository.PlanRepository;
import com.owtunnel.repository.SubscriptionRepository;
import com.owtunnel.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    private PaymentRepository paymentRepository;
    private PaymentMapper paymentMapper;
    private UserRepository userRepository;
    private PlanRepository planRepository;
    private SubscriptionRepository subscriptionRepository;
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        paymentRepository = mock(PaymentRepository.class);
        paymentMapper = mock(PaymentMapper.class);
        userRepository = mock(UserRepository.class);
        planRepository = mock(PlanRepository.class);
        subscriptionRepository = mock(SubscriptionRepository.class);
        paymentService = new PaymentService(paymentRepository, paymentMapper, userRepository, planRepository, subscriptionRepository);
    }

    @Test
    void testGetAll() {
        when(paymentRepository.findAll()).thenReturn(List.of(new Payment()));
        when(paymentMapper.toResponse(any())).thenReturn(new PaymentResponse());

        List<PaymentResponse> result = paymentService.getAll();
        assertEquals(1, result.size());
    }

    @Test
    void testGetByIdSuccess() {
        Payment payment = new Payment();
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentMapper.toResponse(payment)).thenReturn(new PaymentResponse());

        PaymentResponse response = paymentService.getById(1L);
        assertNotNull(response);
    }

    @Test
    void testGetByIdNotFound() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> paymentService.getById(1L));
    }

    @Test
    void testCreate() {
        PaymentRequest request = new PaymentRequest();
        request.setUserId(1L);
        request.setPlanId(2L);
        request.setSubscriptionId(3L);

        User user = new User();
        Plan plan = new Plan();
        Subscription subscription = new Subscription();
        Payment payment = new Payment();
        PaymentResponse response = new PaymentResponse();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(planRepository.findById(2L)).thenReturn(Optional.of(plan));
        when(subscriptionRepository.findById(3L)).thenReturn(Optional.of(subscription));
        when(paymentMapper.toEntity(request, user, plan, subscription)).thenReturn(payment);
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(paymentMapper.toResponse(payment)).thenReturn(response);

        PaymentResponse result = paymentService.create(request);
        assertEquals(response, result);
    }

    @Test
    void testUpdateSuccess() {
        PaymentRequest request = new PaymentRequest();
        request.setUserId(1L);
        request.setPlanId(2L);
        request.setSubscriptionId(3L);
        request.setAmount(BigDecimal.valueOf(100.00));
        request.setCurrency("USD");
        request.setMethod("CREDIT_CARD");
        request.setStatus("COMPLETED");
        request.setTransactionReference("TX123");

        User user = new User();
        Plan plan = new Plan();
        Subscription subscription = new Subscription();
        Payment existing = new Payment();
        PaymentResponse response = new PaymentResponse();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(planRepository.findById(2L)).thenReturn(Optional.of(plan));
        when(subscriptionRepository.findById(3L)).thenReturn(Optional.of(subscription));
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(paymentRepository.save(existing)).thenReturn(existing);
        when(paymentMapper.toResponse(existing)).thenReturn(response);

        PaymentResponse result = paymentService.update(1L, request);
        assertEquals(response, result);
    }

    @Test
    void testUpdateNotFound() {
        PaymentRequest request = new PaymentRequest();
        request.setUserId(1L);
        request.setPlanId(2L);
        request.setSubscriptionId(3L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(planRepository.findById(2L)).thenReturn(Optional.of(new Plan()));
        when(subscriptionRepository.findById(3L)).thenReturn(Optional.of(new Subscription()));
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> paymentService.update(1L, request));
    }

    @Test
    void testDeleteSuccess() {
        Payment payment = new Payment();
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        doNothing().when(paymentRepository).delete(payment);

        assertDoesNotThrow(() -> paymentService.delete(1L));
        verify(paymentRepository).delete(payment);
    }

    @Test
    void testDeleteNotFound() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> paymentService.delete(1L));
    }
}

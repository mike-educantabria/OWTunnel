package com.owtunnel.service;

import com.owtunnel.dto.request.SubscriptionRequest;
import com.owtunnel.dto.response.SubscriptionResponse;
import com.owtunnel.exception.EntityNotFoundException;
import com.owtunnel.mapper.SubscriptionMapper;
import com.owtunnel.model.entity.Plan;
import com.owtunnel.model.entity.Subscription;
import com.owtunnel.model.entity.User;
import com.owtunnel.repository.PlanRepository;
import com.owtunnel.repository.SubscriptionRepository;
import com.owtunnel.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubscriptionServiceTest {

    private SubscriptionRepository subscriptionRepository;
    private SubscriptionMapper subscriptionMapper;
    private UserRepository userRepository;
    private PlanRepository planRepository;
    private SubscriptionService subscriptionService;

    @BeforeEach
    void setUp() {
        subscriptionRepository = mock(SubscriptionRepository.class);
        subscriptionMapper = mock(SubscriptionMapper.class);
        userRepository = mock(UserRepository.class);
        planRepository = mock(PlanRepository.class);
        subscriptionService = new SubscriptionService(subscriptionRepository, subscriptionMapper, userRepository, planRepository);
    }

    @Test
    void testGetAll() {
        when(subscriptionRepository.findAll()).thenReturn(List.of(new Subscription()));
        when(subscriptionMapper.toResponse(any())).thenReturn(new SubscriptionResponse());

        List<SubscriptionResponse> result = subscriptionService.getAll();
        assertEquals(1, result.size());
    }

    @Test
    void testGetByIdSuccess() {
        Subscription subscription = new Subscription();
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));
        when(subscriptionMapper.toResponse(subscription)).thenReturn(new SubscriptionResponse());

        SubscriptionResponse response = subscriptionService.getById(1L);
        assertNotNull(response);
    }

    @Test
    void testGetByIdNotFound() {
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> subscriptionService.getById(1L));
    }

    @Test
    void testCreate() {
        SubscriptionRequest request = new SubscriptionRequest();
        request.setUserId(1L);
        request.setPlanId(2L);

        User user = new User();
        Plan plan = new Plan();
        Subscription entity = new Subscription();
        SubscriptionResponse response = new SubscriptionResponse();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(planRepository.findById(2L)).thenReturn(Optional.of(plan));
        when(subscriptionMapper.toEntity(request, user, plan)).thenReturn(entity);
        when(subscriptionRepository.save(entity)).thenReturn(entity);
        when(subscriptionMapper.toResponse(entity)).thenReturn(response);

        SubscriptionResponse result = subscriptionService.create(request);
        assertEquals(response, result);
    }

    @Test
    void testUpdateSuccess() {
        SubscriptionRequest request = new SubscriptionRequest();
        request.setUserId(1L);
        request.setPlanId(2L);

        User user = new User();
        Plan plan = new Plan();
        Subscription existing = new Subscription();
        Subscription updated = new Subscription();
        SubscriptionResponse response = new SubscriptionResponse();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(planRepository.findById(2L)).thenReturn(Optional.of(plan));
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(subscriptionRepository.save(any())).thenReturn(updated);
        when(subscriptionMapper.toResponse(updated)).thenReturn(response);

        SubscriptionResponse result = subscriptionService.update(1L, request);
        assertEquals(response, result);
    }

    @Test
    void testUpdateNotFound() {
        SubscriptionRequest request = new SubscriptionRequest();
        request.setUserId(1L);
        request.setPlanId(2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(planRepository.findById(2L)).thenReturn(Optional.of(new Plan()));
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> subscriptionService.update(1L, request));
    }

    @Test
    void testDeleteSuccess() {
        Subscription subscription = new Subscription();
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));

        assertDoesNotThrow(() -> subscriptionService.delete(1L));
        verify(subscriptionRepository).delete(subscription);
    }

    @Test
    void testDeleteNotFound() {
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> subscriptionService.delete(1L));
    }
}

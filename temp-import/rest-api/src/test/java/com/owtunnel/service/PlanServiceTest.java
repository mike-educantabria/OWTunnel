package com.owtunnel.service;

import com.owtunnel.dto.request.PlanRequest;
import com.owtunnel.dto.response.PlanResponse;
import com.owtunnel.model.entity.Plan;
import com.owtunnel.exception.EntityNotFoundException;
import com.owtunnel.mapper.PlanMapper;
import com.owtunnel.repository.PlanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlanServiceTest {

    @InjectMocks
    private PlanService planService;

    @Mock
    private PlanRepository planRepository;

    @Mock
    private PlanMapper planMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll_shouldReturnList() {
        List<Plan> plans = List.of(new Plan());
        when(planRepository.findAll()).thenReturn(plans);
        when(planMapper.toResponse(any())).thenReturn(new PlanResponse());

        List<PlanResponse> result = planService.getAll();

        assertEquals(1, result.size());
        verify(planRepository).findAll();
    }

    @Test
    void getById_shouldReturnPlanResponse() {
        Plan plan = new Plan();
        when(planRepository.findById(1L)).thenReturn(Optional.of(plan));
        when(planMapper.toResponse(plan)).thenReturn(new PlanResponse());

        PlanResponse result = planService.getById(1L);

        assertNotNull(result);
        verify(planRepository).findById(1L);
    }

    @Test
    void getById_shouldThrowException() {
        when(planRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> planService.getById(1L));
    }

    @Test
    void create_shouldReturnSavedPlanResponse() {
        PlanRequest request = new PlanRequest();
        Plan entity = new Plan();
        PlanResponse response = new PlanResponse();

        when(planMapper.toEntity(request)).thenReturn(entity);
        when(planRepository.save(entity)).thenReturn(entity);
        when(planMapper.toResponse(entity)).thenReturn(response);

        PlanResponse result = planService.create(request);

        assertEquals(response, result);
    }

    @Test
    void update_shouldReturnUpdatedPlanResponse() {
        PlanRequest request = new PlanRequest();
        request.setName("New Name");

        Plan existingPlan = new Plan();
        existingPlan.setId(1L);
        Plan updatedPlan = new Plan();
        PlanResponse updatedResponse = new PlanResponse();

        when(planRepository.findById(1L)).thenReturn(Optional.of(existingPlan));
        when(planMapper.mapStringToCurrency(any())).thenReturn(null);
        when(planRepository.save(existingPlan)).thenReturn(updatedPlan);
        when(planMapper.toResponse(updatedPlan)).thenReturn(updatedResponse);

        PlanResponse result = planService.update(1L, request);

        assertEquals(updatedResponse, result);
    }

    @Test
    void update_shouldThrowIfNotFound() {
        when(planRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> planService.update(1L, new PlanRequest()));
    }

    @Test
    void delete_shouldRemovePlan() {
        Plan plan = new Plan();
        when(planRepository.findById(1L)).thenReturn(Optional.of(plan));

        planService.delete(1L);

        verify(planRepository).delete(plan);
    }

    @Test
    void delete_shouldThrowIfNotFound() {
        when(planRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> planService.delete(1L));
    }
}
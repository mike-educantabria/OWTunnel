package com.owtunnel.service;

import com.owtunnel.dto.request.PlanRequest;
import com.owtunnel.dto.response.PlanResponse;
import com.owtunnel.exception.EntityNotFoundException;
import com.owtunnel.mapper.PlanMapper;
import com.owtunnel.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PlanService {

    private final PlanRepository planRepository;
    private final PlanMapper planMapper;

    public List<PlanResponse> getAll() {
        return planRepository.findAll().stream()
                .map(planMapper::toResponse)
                .toList();
    }

    public PlanResponse getById(Long id) {
        return planMapper.toResponse(planRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plan", id))
        );
    }

    @Transactional
    public PlanResponse create(PlanRequest request) {
        return planMapper.toResponse(planRepository.save(
                planMapper.toEntity(request)
        ));
    }

    @Transactional
    public PlanResponse update(Long id, PlanRequest request) {
        return planRepository.findById(id)
                .map(existingPlan -> {
                    existingPlan.setName(request.getName());
                    existingPlan.setDescription(request.getDescription());
                    existingPlan.setPrice(request.getPrice());
                    existingPlan.setCurrency(planMapper.mapStringToCurrency(request.getCurrency()));
                    existingPlan.setDurationDays(request.getDurationDays());
                    existingPlan.setIsActive(request.getIsActive());
                    return planMapper.toResponse(planRepository.save(existingPlan));
                })
                .orElseThrow(() -> new EntityNotFoundException("Plan", id));
    }

    @Transactional
    public void delete(Long id) {
        var plan = planRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plan", id));
        planRepository.delete(plan);
    }
}

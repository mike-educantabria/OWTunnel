package com.owtunnel.service;

import com.owtunnel.dto.request.SubscriptionRequest;
import com.owtunnel.dto.response.SubscriptionResponse;
import com.owtunnel.exception.EntityNotFoundException;
import com.owtunnel.mapper.SubscriptionMapper;
import com.owtunnel.repository.PlanRepository;
import com.owtunnel.repository.SubscriptionRepository;
import com.owtunnel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;

    public List<SubscriptionResponse> getAll() {
        return subscriptionRepository.findAll().stream()
                .map(subscriptionMapper::toResponse)
                .toList();
    }

    public SubscriptionResponse getById(Long id) {
        return subscriptionMapper.toResponse(subscriptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subscription", id))
        );
    }

    @Transactional
    public SubscriptionResponse create(SubscriptionRequest request) {
        var user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User", request.getUserId()));
        var plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() -> new EntityNotFoundException("Plan", request.getPlanId()));

        return subscriptionMapper.toResponse(subscriptionRepository.save(
                subscriptionMapper.toEntity(request, user, plan)
        ));
    }

    @Transactional
    public SubscriptionResponse update(Long id, SubscriptionRequest request) {
        var user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User", request.getUserId()));
        var plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() -> new EntityNotFoundException("Plan", request.getPlanId()));

        return subscriptionRepository.findById(id)
                .map(existingSubscription -> {
                    existingSubscription.setUser(user);
                    existingSubscription.setPlan(plan);
                    existingSubscription.setStatus(subscriptionMapper.mapStringToStatus(request.getStatus()));
                    existingSubscription.setAutoRenew(request.getAutoRenew());
                    existingSubscription.setExpiresAt(request.getExpiresAt());
                    return subscriptionMapper.toResponse(subscriptionRepository.save(existingSubscription));
                })
                .orElseThrow(() -> new EntityNotFoundException("Subscription", id));
    }

    @Transactional
    public void delete(Long id) {
        var subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subscription", id));
        subscriptionRepository.delete(subscription);
    }
}

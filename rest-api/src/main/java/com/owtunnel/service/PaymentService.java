package com.owtunnel.service;

import com.owtunnel.dto.request.PaymentRequest;
import com.owtunnel.dto.response.PaymentResponse;
import com.owtunnel.exception.EntityNotFoundException;
import com.owtunnel.mapper.PaymentMapper;
import com.owtunnel.repository.PaymentRepository;
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
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final SubscriptionRepository subscriptionRepository;

    public List<PaymentResponse> getAll() {
        return paymentRepository.findAll().stream()
                .map(paymentMapper::toResponse)
                .toList();
    }

    public PaymentResponse getById(Long id) {
        return paymentMapper.toResponse(paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment", id))
        );
    }

    @Transactional
    public PaymentResponse create(PaymentRequest request) {
        var user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User", request.getUserId()));
        var plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() -> new EntityNotFoundException("Plan", request.getPlanId()));
        var subscription = subscriptionRepository.findById(request.getSubscriptionId())
                .orElseThrow(() -> new EntityNotFoundException("Subscription", request.getSubscriptionId()));

        return paymentMapper.toResponse(paymentRepository.save(
                paymentMapper.toEntity(request, user, plan, subscription)
        ));
    }

    @Transactional
    public PaymentResponse update(Long id, PaymentRequest request) {
        var user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User", request.getUserId()));
        var plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() -> new EntityNotFoundException("Plan", request.getPlanId()));
        var subscription = subscriptionRepository.findById(request.getSubscriptionId())
                .orElseThrow(() -> new EntityNotFoundException("Subscription", request.getSubscriptionId()));

        return paymentRepository.findById(id)
                .map(existingPayment -> {
                    existingPayment.setUser(user);
                    existingPayment.setPlan(plan);
                    existingPayment.setSubscription(subscription);
                    existingPayment.setAmount(request.getAmount());
                    existingPayment.setCurrency(paymentMapper.mapStringToCurrency(request.getCurrency()));
                    existingPayment.setMethod(paymentMapper.mapStringToMethod(request.getMethod()));
                    existingPayment.setStatus(paymentMapper.mapStringToStatus(request.getStatus()));
                    existingPayment.setTransactionReference(request.getTransactionReference());
                    return paymentMapper.toResponse(paymentRepository.save(existingPayment));
                })
                .orElseThrow(() -> new EntityNotFoundException("Payment", id));
    }

    @Transactional
    public void delete(Long id) {
        var payment = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment", id));
        paymentRepository.delete(payment);
    }
}

package com.owtunnel.mapper;

import com.owtunnel.dto.request.PaymentRequest;
import com.owtunnel.dto.response.PaymentResponse;
import com.owtunnel.model.entity.Payment;
import com.owtunnel.model.entity.Plan;
import com.owtunnel.model.entity.Subscription;
import com.owtunnel.model.entity.User;
import com.owtunnel.model.enums.Currency;
import com.owtunnel.model.enums.PaymentMethod;
import com.owtunnel.model.enums.PaymentStatus;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "plan", ignore = true)
    @Mapping(target = "subscription", ignore = true)
    @Mapping(target = "currency", expression = "java(mapStringToCurrency(paymentRequest.getCurrency()))")
    @Mapping(target = "method", expression = "java(mapStringToMethod(paymentRequest.getMethod()))")
    @Mapping(target = "status", expression = "java(mapStringToStatus(paymentRequest.getStatus()))")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Payment toEntity(PaymentRequest paymentRequest, @Context User user, @Context Plan plan, @Context Subscription subscription);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "planId", source = "plan.id")
    @Mapping(target = "subscriptionId", source = "subscription.id")
    @Mapping(target = "currency", expression = "java(mapCurrencyToString(payment.getCurrency()))")
    @Mapping(target = "method", expression = "java(mapMethodToString(payment.getMethod()))")
    @Mapping(target = "status", expression = "java(mapStatusToString(payment.getStatus()))")
    PaymentResponse toResponse(Payment payment);

    @AfterMapping
    default void assignContextReferences(@MappingTarget Payment payment, @Context User user, @Context Plan plan, @Context Subscription subscription) {
        payment.setUser(user);
        payment.setPlan(plan);
        payment.setSubscription(subscription);
    }

    default Currency mapStringToCurrency(String value) {
        try {
            return value != null ? Currency.valueOf(value) : Currency.USD;
        } catch (IllegalArgumentException e) {
            return Currency.USD;
        }
    }

    default PaymentMethod mapStringToMethod(String value) {
        try {
            return value != null ? PaymentMethod.valueOf(value) : PaymentMethod.PAYPAL;
        } catch (IllegalArgumentException e) {
            return PaymentMethod.PAYPAL;
        }
    }

    default PaymentStatus mapStringToStatus(String value) {
        try {
            return value != null ? PaymentStatus.valueOf(value) : PaymentStatus.PENDING;
        } catch (IllegalArgumentException e) {
            return PaymentStatus.PENDING;
        }
    }

    default String mapCurrencyToString(Currency currency) {
        return currency != null ? currency.name() : null;
    }

    default String mapMethodToString(PaymentMethod method) {
        return method != null ? method.name() : null;
    }

    default String mapStatusToString(PaymentStatus status) {
        return status != null ? status.name() : null;
    }
}

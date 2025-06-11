package com.owtunnel.mapper;

import com.owtunnel.dto.request.SubscriptionRequest;
import com.owtunnel.dto.response.SubscriptionResponse;
import com.owtunnel.model.entity.Plan;
import com.owtunnel.model.entity.Subscription;
import com.owtunnel.model.entity.User;
import com.owtunnel.model.enums.SubscriptionStatus;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "plan", ignore = true)
    @Mapping(target = "status", expression = "java(mapStringToStatus(subscriptionRequest.getStatus()))")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "expiresAt", ignore = true)
    Subscription toEntity(SubscriptionRequest subscriptionRequest, @Context User user, @Context Plan plan);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "planId", source = "plan.id")
    @Mapping(target = "status", expression = "java(mapStatusToString(subscription.getStatus()))")
    SubscriptionResponse toResponse(Subscription subscription);

    @AfterMapping
    default void assignContextObjects(@MappingTarget Subscription subscription, @Context User user, @Context Plan plan) {
        subscription.setUser(user);
        subscription.setPlan(plan);
    }

    default SubscriptionStatus mapStringToStatus(String statusStr) {
        try {
            return statusStr != null ? SubscriptionStatus.valueOf(statusStr) : SubscriptionStatus.PENDING;
        } catch (IllegalArgumentException e) {
            return SubscriptionStatus.PENDING;
        }
    }

    default String mapStatusToString(SubscriptionStatus status) {
        return status != null ? status.name() : null;
    }
}

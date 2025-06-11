package com.owtunnel.mapper;

import com.owtunnel.dto.request.PlanRequest;
import com.owtunnel.dto.response.PlanResponse;
import com.owtunnel.model.entity.Plan;
import com.owtunnel.model.enums.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlanMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "currency", expression = "java(mapStringToCurrency(planRequest.getCurrency()))")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Plan toEntity(PlanRequest planRequest);

    @Mapping(target = "currency", expression = "java(mapCurrencyToString(plan.getCurrency()))")
    PlanResponse toResponse(Plan plan);

    default Currency mapStringToCurrency(String currencyStr) {
        try {
            return currencyStr != null ? Currency.valueOf(currencyStr) : Currency.USD;
        } catch (IllegalArgumentException e) {
            return Currency.USD;
        }
    }

    default String mapCurrencyToString(Currency currency) {
        return currency != null ? currency.name() : null;
    }
}

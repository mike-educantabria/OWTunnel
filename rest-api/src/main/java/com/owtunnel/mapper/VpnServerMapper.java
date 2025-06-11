package com.owtunnel.mapper;

import com.owtunnel.dto.request.VpnServerRequest;
import com.owtunnel.dto.response.VpnServerResponse;
import com.owtunnel.model.entity.VpnServer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VpnServerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    VpnServer toEntity(VpnServerRequest vpnServerRequest);

    VpnServerResponse toResponse(VpnServer vpnServer);
}

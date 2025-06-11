package com.owtunnel.mapper;

import com.owtunnel.dto.request.ConnectionRequest;
import com.owtunnel.dto.response.ConnectionResponse;
import com.owtunnel.model.entity.Connection;
import com.owtunnel.model.entity.User;
import com.owtunnel.model.entity.VpnServer;
import com.owtunnel.model.enums.ConnectionStatus;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ConnectionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "vpnServer", ignore = true)
    @Mapping(target = "status", expression = "java(mapStringToStatus(connectionRequest.getStatus()))")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Connection toEntity(ConnectionRequest connectionRequest, @Context User user, @Context VpnServer vpnServer);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "vpnServerId", source = "vpnServer.id")
    @Mapping(target = "status", expression = "java(mapStatusToString(connection.getStatus()))")
    ConnectionResponse toResponse(Connection connection);

    @AfterMapping
    default void assignContextObjects(@MappingTarget Connection connection, @Context User user, @Context VpnServer vpnServer) {
        connection.setUser(user);
        connection.setVpnServer(vpnServer);
    }

    default ConnectionStatus mapStringToStatus(String statusStr) {
        try {
            return statusStr != null ? ConnectionStatus.valueOf(statusStr) : ConnectionStatus.CONNECTED;
        } catch (IllegalArgumentException e) {
            return ConnectionStatus.CONNECTED;
        }
    }

    default String mapStatusToString(ConnectionStatus status) {
        return status != null ? status.name() : null;
    }
}

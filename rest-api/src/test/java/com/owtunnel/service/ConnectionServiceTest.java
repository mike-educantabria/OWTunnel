package com.owtunnel.service;

import com.owtunnel.dto.request.ConnectionRequest;
import com.owtunnel.dto.response.ConnectionResponse;
import com.owtunnel.exception.EntityNotFoundException;
import com.owtunnel.mapper.ConnectionMapper;
import com.owtunnel.model.entity.Connection;
import com.owtunnel.model.entity.User;
import com.owtunnel.model.entity.VpnServer;
import com.owtunnel.repository.ConnectionRepository;
import com.owtunnel.repository.UserRepository;
import com.owtunnel.repository.VpnServerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConnectionServiceTest {

    private ConnectionRepository connectionRepository;
    private ConnectionMapper connectionMapper;
    private UserRepository userRepository;
    private VpnServerRepository vpnServerRepository;
    private ConnectionService connectionService;

    @BeforeEach
    void setUp() {
        connectionRepository = mock(ConnectionRepository.class);
        connectionMapper = mock(ConnectionMapper.class);
        userRepository = mock(UserRepository.class);
        vpnServerRepository = mock(VpnServerRepository.class);
        connectionService = new ConnectionService(connectionRepository, connectionMapper, userRepository, vpnServerRepository);
    }

    @Test
    void testGetAll() {
        when(connectionRepository.findAll()).thenReturn(List.of(new Connection()));
        when(connectionMapper.toResponse(any())).thenReturn(new ConnectionResponse());

        List<ConnectionResponse> result = connectionService.getAll();
        assertEquals(1, result.size());
    }

    @Test
    void testGetByIdSuccess() {
        Connection connection = new Connection();
        when(connectionRepository.findById(1L)).thenReturn(Optional.of(connection));
        when(connectionMapper.toResponse(connection)).thenReturn(new ConnectionResponse());

        ConnectionResponse result = connectionService.getById(1L);
        assertNotNull(result);
    }

    @Test
    void testGetByIdNotFound() {
        when(connectionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> connectionService.getById(1L));
    }

    @Test
    void testCreate() {
        ConnectionRequest request = new ConnectionRequest();
        request.setUserId(1L);
        request.setVpnServerId(2L);

        User user = new User();
        VpnServer vpnServer = new VpnServer();
        Connection entity = new Connection();
        ConnectionResponse response = new ConnectionResponse();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(vpnServerRepository.findById(2L)).thenReturn(Optional.of(vpnServer));
        when(connectionMapper.toEntity(request, user, vpnServer)).thenReturn(entity);
        when(connectionRepository.save(entity)).thenReturn(entity);
        when(connectionMapper.toResponse(entity)).thenReturn(response);

        ConnectionResponse result = connectionService.create(request);
        assertEquals(response, result);
    }

    @Test
    void testUpdateSuccess() {
        ConnectionRequest request = new ConnectionRequest();
        request.setUserId(1L);
        request.setVpnServerId(2L);

        User user = new User();
        VpnServer vpnServer = new VpnServer();
        Connection existing = new Connection();
        Connection updated = new Connection();
        ConnectionResponse response = new ConnectionResponse();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(vpnServerRepository.findById(2L)).thenReturn(Optional.of(vpnServer));
        when(connectionRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(connectionRepository.save(any())).thenReturn(updated);
        when(connectionMapper.toResponse(updated)).thenReturn(response);

        ConnectionResponse result = connectionService.update(1L, request);
        assertEquals(response, result);
    }

    @Test
    void testUpdateNotFound() {
        ConnectionRequest request = new ConnectionRequest();
        request.setUserId(1L);
        request.setVpnServerId(2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(vpnServerRepository.findById(2L)).thenReturn(Optional.of(new VpnServer()));
        when(connectionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> connectionService.update(1L, request));
    }

    @Test
    void testDeleteSuccess() {
        Connection connection = new Connection();
        when(connectionRepository.findById(1L)).thenReturn(Optional.of(connection));

        assertDoesNotThrow(() -> connectionService.delete(1L));
        verify(connectionRepository).delete(connection);
    }

    @Test
    void testDeleteNotFound() {
        when(connectionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> connectionService.delete(1L));
    }
}

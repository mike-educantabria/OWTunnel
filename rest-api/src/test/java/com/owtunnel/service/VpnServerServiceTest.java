package com.owtunnel.service;

import com.owtunnel.dto.request.VpnServerRequest;
import com.owtunnel.dto.response.VpnServerResponse;
import com.owtunnel.exception.EntityNotFoundException;
import com.owtunnel.mapper.VpnServerMapper;
import com.owtunnel.model.entity.VpnServer;
import com.owtunnel.repository.VpnServerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VpnServerServiceTest {

    private VpnServerRepository vpnServerRepository;
    private VpnServerMapper vpnServerMapper;
    private VpnServerService vpnServerService;

    @BeforeEach
    void setUp() {
        vpnServerRepository = mock(VpnServerRepository.class);
        vpnServerMapper = mock(VpnServerMapper.class);
        vpnServerService = new VpnServerService(vpnServerRepository, vpnServerMapper);
    }

    @Test
    void testGetAll() {
        when(vpnServerRepository.findAll()).thenReturn(List.of(new VpnServer()));
        when(vpnServerMapper.toResponse(any())).thenReturn(new VpnServerResponse());

        List<VpnServerResponse> result = vpnServerService.getAll();
        assertEquals(1, result.size());
    }

    @Test
    void testGetByIdSuccess() {
        VpnServer vpnServer = new VpnServer();
        when(vpnServerRepository.findById(1L)).thenReturn(Optional.of(vpnServer));
        when(vpnServerMapper.toResponse(vpnServer)).thenReturn(new VpnServerResponse());

        VpnServerResponse response = vpnServerService.getById(1L);
        assertNotNull(response);
    }

    @Test
    void testGetByIdNotFound() {
        when(vpnServerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> vpnServerService.getById(1L));
    }

    @Test
    void testCreate() {
        VpnServerRequest request = new VpnServerRequest();
        VpnServer entity = new VpnServer();
        VpnServerResponse response = new VpnServerResponse();

        when(vpnServerMapper.toEntity(request)).thenReturn(entity);
        when(vpnServerRepository.save(entity)).thenReturn(entity);
        when(vpnServerMapper.toResponse(entity)).thenReturn(response);

        VpnServerResponse result = vpnServerService.create(request);
        assertEquals(response, result);
    }

    @Test
    void testUpdateSuccess() {
        VpnServerRequest request = new VpnServerRequest();
        request.setIpAddress("123.123.123.123");

        VpnServer existing = new VpnServer();
        VpnServer updated = new VpnServer();
        VpnServerResponse response = new VpnServerResponse();

        when(vpnServerRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(vpnServerMapper.toEntity(request)).thenReturn(updated);
        when(vpnServerRepository.save(any())).thenReturn(updated);
        when(vpnServerMapper.toResponse(updated)).thenReturn(response);

        VpnServerResponse result = vpnServerService.update(1L, request);
        assertEquals(response, result);
    }

    @Test
    void testUpdateNotFound() {
        VpnServerRequest request = new VpnServerRequest();
        request.setIpAddress("123.123.123.123");

        when(vpnServerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> vpnServerService.update(1L, request));
    }

    @Test
    void testDeleteSuccess() {
        VpnServer vpnServer = new VpnServer();
        when(vpnServerRepository.findById(1L)).thenReturn(Optional.of(vpnServer));

        assertDoesNotThrow(() -> vpnServerService.delete(1L));
        verify(vpnServerRepository).delete(vpnServer);
    }

    @Test
    void testDeleteNotFound() {
        when(vpnServerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> vpnServerService.delete(1L));
    }
}

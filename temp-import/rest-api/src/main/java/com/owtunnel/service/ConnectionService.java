package com.owtunnel.service;

import com.owtunnel.dto.request.ConnectionRequest;
import com.owtunnel.dto.response.ConnectionResponse;
import com.owtunnel.exception.EntityNotFoundException;
import com.owtunnel.mapper.ConnectionMapper;
import com.owtunnel.repository.ConnectionRepository;
import com.owtunnel.repository.UserRepository;
import com.owtunnel.repository.VpnServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ConnectionService {

    private final ConnectionRepository connectionRepository;
    private final ConnectionMapper connectionMapper;
    private final UserRepository userRepository;
    private final VpnServerRepository vpnServerRepository;

    public List<ConnectionResponse> getAll() {
        return connectionRepository.findAll().stream()
                .map(connectionMapper::toResponse)
                .toList();
    }

    public ConnectionResponse getById(Long id) {
        return connectionMapper.toResponse(connectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Connection", id))
        );
    }

    @Transactional
    public ConnectionResponse create(ConnectionRequest request) {
        var user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User", request.getUserId()));
        var vpnServer = vpnServerRepository.findById(request.getVpnServerId())
                .orElseThrow(() -> new EntityNotFoundException("VPN Server", request.getVpnServerId()));

        return connectionMapper.toResponse(connectionRepository.save(
                connectionMapper.toEntity(request, user, vpnServer)
        ));
    }

    @Transactional
    public ConnectionResponse update(Long id, ConnectionRequest request) {
        var user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User", request.getUserId()));
        var vpnServer = vpnServerRepository.findById(request.getVpnServerId())
                .orElseThrow(() -> new EntityNotFoundException("VPN Server", request.getVpnServerId()));

        return connectionRepository.findById(id)
                .map(existingConnection -> {
                    existingConnection.setUser(user);
                    existingConnection.setVpnServer(vpnServer);
                    existingConnection.setDeviceInfo(request.getDeviceInfo());
                    existingConnection.setStatus(connectionMapper.mapStringToStatus(request.getStatus()));
                    return connectionMapper.toResponse(connectionRepository.save(existingConnection));
                })
                .orElseThrow(() -> new EntityNotFoundException("Connection", id));
    }

    @Transactional
    public void delete(Long id) {
        var connection = connectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Connection", id));
        connectionRepository.delete(connection);
    }
}

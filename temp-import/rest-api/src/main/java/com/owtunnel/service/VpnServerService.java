package com.owtunnel.service;

import com.owtunnel.dto.request.VpnServerRequest;
import com.owtunnel.dto.response.VpnServerResponse;
import com.owtunnel.exception.EntityNotFoundException;
import com.owtunnel.mapper.VpnServerMapper;
import com.owtunnel.repository.VpnServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class VpnServerService {

    private final VpnServerRepository vpnServerRepository;
    private final VpnServerMapper vpnServerMapper;

    public List<VpnServerResponse> getAll() {
        return vpnServerRepository.findAll().stream()
                .map(vpnServerMapper::toResponse)
                .toList();
    }

    public VpnServerResponse getById(Long id) {
        return vpnServerMapper.toResponse(vpnServerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("VPN Server", id))
        );
    }

    @Transactional
    public VpnServerResponse create(VpnServerRequest request) {
        return vpnServerMapper.toResponse(vpnServerRepository.save(
                vpnServerMapper.toEntity(request)
        ));
    }

    @Transactional
    public VpnServerResponse update(Long id, VpnServerRequest request) {
        return vpnServerRepository.findById(id)
                .map(existingVpnServer -> {
                    existingVpnServer.setCountry(request.getCountry());
                    existingVpnServer.setCity(request.getCity());
                    existingVpnServer.setHostname(request.getHostname());
                    existingVpnServer.setIpAddress(request.getIpAddress());
                    existingVpnServer.setConfigFileUrl(request.getConfigFileUrl());
                    existingVpnServer.setIsFree(request.getIsFree());
                    existingVpnServer.setIsActive(request.getIsActive());
                    return vpnServerMapper.toResponse(vpnServerRepository.save(existingVpnServer));
                })
                .orElseThrow(() -> new EntityNotFoundException("VPN Server", id));
    }

    @Transactional
    public void delete(Long id) {
        var vpnServer = vpnServerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("VPN Server", id));
        vpnServerRepository.delete(vpnServer);
    }
}

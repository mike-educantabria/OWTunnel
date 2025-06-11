package com.owtunnel.service;

import com.owtunnel.dto.request.LoginAttemptRequest;
import com.owtunnel.dto.response.LoginAttemptResponse;
import com.owtunnel.exception.EntityNotFoundException;
import com.owtunnel.mapper.LoginAttemptMapper;
import com.owtunnel.repository.LoginAttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LoginAttemptService {

    private final LoginAttemptRepository loginAttemptRepository;
    private final LoginAttemptMapper loginAttemptMapper;

    public List<LoginAttemptResponse> getAll() {
        return loginAttemptRepository.findAll().stream()
                .map(loginAttemptMapper::toResponse)
                .toList();
    }

    public LoginAttemptResponse getById(Long id) {
        return loginAttemptMapper.toResponse(loginAttemptRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Login Attempt", id))
        );
    }

    @Transactional
    public LoginAttemptResponse create(LoginAttemptRequest request) {
        return loginAttemptMapper.toResponse(loginAttemptRepository.save(
                loginAttemptMapper.toEntity(request)
        ));
    }

    @Transactional
    public LoginAttemptResponse update(Long id, LoginAttemptRequest request) {
        return loginAttemptRepository.findById(id)
                .map(existingAttempt -> {
                    existingAttempt.setIpAddress(request.getIpAddress());
                    existingAttempt.setAttemptCount(request.getAttemptCount());
                    existingAttempt.setBlockedUntil(loginAttemptMapper.toEntity(request).getBlockedUntil());
                    return loginAttemptMapper.toResponse(loginAttemptRepository.save(existingAttempt));
                })
                .orElseThrow(() -> new EntityNotFoundException("Login Attempt", id));
    }

    @Transactional
    public void delete(Long id) {
        var loginAttempt = loginAttemptRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Login Attempt", id));
        loginAttemptRepository.delete(loginAttempt);
    }
}

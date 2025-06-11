package com.owtunnel.service;

import com.owtunnel.dto.request.PasswordResetRequest;
import com.owtunnel.dto.response.PasswordResetResponse;
import com.owtunnel.exception.EntityNotFoundException;
import com.owtunnel.mapper.PasswordResetMapper;
import com.owtunnel.repository.PasswordResetRepository;
import com.owtunnel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.owtunnel.util.TokenGenerator.generateToken;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PasswordResetService {

    private final PasswordResetRepository passwordResetRepository;
    private final PasswordResetMapper passwordResetMapper;
    private final UserRepository userRepository;

    public List<PasswordResetResponse> getAll() {
        return passwordResetRepository.findAll().stream()
                .map(passwordResetMapper::toResponse)
                .toList();
    }

    public PasswordResetResponse getById(Long id) {
        return passwordResetMapper.toResponse(passwordResetRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Password Reset", id))
        );
    }

    @Transactional
    public PasswordResetResponse create(PasswordResetRequest request) {
        var user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User", request.getUserId()));

        return passwordResetMapper.toResponse(passwordResetRepository.save(
                passwordResetMapper.toEntity(request, user)
        ));
    }

    @Transactional
    public PasswordResetResponse update(Long id, PasswordResetRequest request) {
        var user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User", request.getUserId()));

        return passwordResetRepository.findById(id)
                .map(existingPasswordReset -> {
                    existingPasswordReset.setUser(user);
                    existingPasswordReset.setResetToken(generateToken(32));
                    return passwordResetMapper.toResponse(passwordResetRepository.save(existingPasswordReset));
                })
                .orElseThrow(() -> new EntityNotFoundException("Password Reset", id));
    }

    @Transactional
    public void delete(Long id) {
        var existingPasswordReset = passwordResetRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Password Reset", id));
        passwordResetRepository.delete(existingPasswordReset);
    }
}

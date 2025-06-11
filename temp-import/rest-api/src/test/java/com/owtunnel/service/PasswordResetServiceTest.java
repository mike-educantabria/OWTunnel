package com.owtunnel.service;

import com.owtunnel.dto.request.PasswordResetRequest;
import com.owtunnel.dto.response.PasswordResetResponse;
import com.owtunnel.model.entity.PasswordReset;
import com.owtunnel.model.entity.User;
import com.owtunnel.exception.EntityNotFoundException;
import com.owtunnel.mapper.PasswordResetMapper;
import com.owtunnel.repository.PasswordResetRepository;
import com.owtunnel.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PasswordResetServiceTest {

    private PasswordResetRepository passwordResetRepository;
    private PasswordResetMapper passwordResetMapper;
    private UserRepository userRepository;
    private PasswordResetService passwordResetService;

    @BeforeEach
    void setUp() {
        passwordResetRepository = mock(PasswordResetRepository.class);
        passwordResetMapper = mock(PasswordResetMapper.class);
        userRepository = mock(UserRepository.class);
        passwordResetService = new PasswordResetService(passwordResetRepository, passwordResetMapper, userRepository);
    }

    @Test
    void testGetAll() {
        when(passwordResetRepository.findAll()).thenReturn(List.of(new PasswordReset()));
        when(passwordResetMapper.toResponse(any())).thenReturn(new PasswordResetResponse());

        List<PasswordResetResponse> result = passwordResetService.getAll();
        assertEquals(1, result.size());
    }

    @Test
    void testGetByIdSuccess() {
        PasswordReset reset = new PasswordReset();
        when(passwordResetRepository.findById(1L)).thenReturn(Optional.of(reset));
        when(passwordResetMapper.toResponse(reset)).thenReturn(new PasswordResetResponse());

        PasswordResetResponse response = passwordResetService.getById(1L);
        assertNotNull(response);
    }

    @Test
    void testGetByIdNotFound() {
        when(passwordResetRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> passwordResetService.getById(1L));
    }

    @Test
    void testCreate() {
        PasswordResetRequest request = new PasswordResetRequest();
        request.setUserId(1L);
        User user = new User();
        PasswordReset reset = new PasswordReset();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordResetMapper.toEntity(request, user)).thenReturn(reset);
        when(passwordResetRepository.save(reset)).thenReturn(reset);
        when(passwordResetMapper.toResponse(reset)).thenReturn(new PasswordResetResponse());

        PasswordResetResponse response = passwordResetService.create(request);
        assertNotNull(response);
    }

    @Test
    void testUpdateSuccess() {
        PasswordResetRequest request = new PasswordResetRequest();
        request.setUserId(1L);
        User user = new User();
        PasswordReset reset = new PasswordReset();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordResetRepository.findById(1L)).thenReturn(Optional.of(reset));
        when(passwordResetRepository.save(any())).thenReturn(reset);
        when(passwordResetMapper.toResponse(reset)).thenReturn(new PasswordResetResponse());

        PasswordResetResponse response = passwordResetService.update(1L, request);
        assertNotNull(response);
    }

    @Test
    void testUpdateNotFound() {
        PasswordResetRequest request = new PasswordResetRequest();
        request.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(passwordResetRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> passwordResetService.update(1L, request));
    }

    @Test
    void testDeleteSuccess() {
        PasswordReset reset = new PasswordReset();
        when(passwordResetRepository.findById(1L)).thenReturn(Optional.of(reset));
        doNothing().when(passwordResetRepository).delete(reset);

        assertDoesNotThrow(() -> passwordResetService.delete(1L));
    }

    @Test
    void testDeleteNotFound() {
        when(passwordResetRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> passwordResetService.delete(1L));
    }
}
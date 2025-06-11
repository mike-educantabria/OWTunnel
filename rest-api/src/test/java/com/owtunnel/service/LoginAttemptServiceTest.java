package com.owtunnel.service;

import com.owtunnel.dto.request.LoginAttemptRequest;
import com.owtunnel.dto.response.LoginAttemptResponse;
import com.owtunnel.exception.EntityNotFoundException;
import com.owtunnel.mapper.LoginAttemptMapper;
import com.owtunnel.model.entity.LoginAttempt;
import com.owtunnel.repository.LoginAttemptRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginAttemptServiceTest {

    private LoginAttemptRepository loginAttemptRepository;
    private LoginAttemptMapper loginAttemptMapper;
    private LoginAttemptService loginAttemptService;

    @BeforeEach
    void setUp() {
        loginAttemptRepository = mock(LoginAttemptRepository.class);
        loginAttemptMapper = mock(LoginAttemptMapper.class);
        loginAttemptService = new LoginAttemptService(loginAttemptRepository, loginAttemptMapper);
    }

    @Test
    void testGetAll() {
        when(loginAttemptRepository.findAll()).thenReturn(List.of(new LoginAttempt()));
        when(loginAttemptMapper.toResponse(any())).thenReturn(new LoginAttemptResponse());

        List<LoginAttemptResponse> result = loginAttemptService.getAll();
        assertEquals(1, result.size());
    }

    @Test
    void testGetByIdSuccess() {
        LoginAttempt loginAttempt = new LoginAttempt();
        when(loginAttemptRepository.findById(1L)).thenReturn(Optional.of(loginAttempt));
        when(loginAttemptMapper.toResponse(loginAttempt)).thenReturn(new LoginAttemptResponse());

        LoginAttemptResponse response = loginAttemptService.getById(1L);
        assertNotNull(response);
    }

    @Test
    void testGetByIdNotFound() {
        when(loginAttemptRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> loginAttemptService.getById(1L));
    }

    @Test
    void testCreate() {
        LoginAttemptRequest request = new LoginAttemptRequest();
        LoginAttempt entity = new LoginAttempt();
        LoginAttemptResponse response = new LoginAttemptResponse();

        when(loginAttemptMapper.toEntity(request)).thenReturn(entity);
        when(loginAttemptRepository.save(entity)).thenReturn(entity);
        when(loginAttemptMapper.toResponse(entity)).thenReturn(response);

        LoginAttemptResponse result = loginAttemptService.create(request);
        assertEquals(response, result);
    }

    @Test
    void testUpdateSuccess() {
        LoginAttemptRequest request = new LoginAttemptRequest();
        request.setIpAddress("127.0.0.1");
        request.setAttemptCount(3);

        LoginAttempt existing = new LoginAttempt();
        LoginAttempt updated = new LoginAttempt();
        LoginAttemptResponse response = new LoginAttemptResponse();

        when(loginAttemptRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(loginAttemptMapper.toEntity(request)).thenReturn(updated);
        when(loginAttemptRepository.save(any())).thenReturn(updated);
        when(loginAttemptMapper.toResponse(updated)).thenReturn(response);

        LoginAttemptResponse result = loginAttemptService.update(1L, request);
        assertEquals(response, result);
    }

    @Test
    void testUpdateNotFound() {
        LoginAttemptRequest request = new LoginAttemptRequest();
        request.setIpAddress("127.0.0.1");

        when(loginAttemptRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> loginAttemptService.update(1L, request));
    }

    @Test
    void testDeleteSuccess() {
        LoginAttempt loginAttempt = new LoginAttempt();
        when(loginAttemptRepository.findById(1L)).thenReturn(Optional.of(loginAttempt));
        doNothing().when(loginAttemptRepository).delete(loginAttempt);

        assertDoesNotThrow(() -> loginAttemptService.delete(1L));
    }

    @Test
    void testDeleteNotFound() {
        when(loginAttemptRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> loginAttemptService.delete(1L));
    }
}

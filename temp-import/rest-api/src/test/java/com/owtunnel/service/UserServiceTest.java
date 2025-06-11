package com.owtunnel.service;

import com.owtunnel.dto.request.UserRequest;
import com.owtunnel.dto.response.UserResponse;
import com.owtunnel.exception.EntityNotFoundException;
import com.owtunnel.mapper.UserMapper;
import com.owtunnel.model.entity.User;
import com.owtunnel.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userMapper = mock(UserMapper.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserService(userRepository, userMapper, passwordEncoder);
    }

    @Test
    void testGetAll() {
        when(userRepository.findAll()).thenReturn(List.of(new User()));
        when(userMapper.toResponse(any())).thenReturn(new UserResponse());

        List<UserResponse> result = userService.getAll();
        assertEquals(1, result.size());
    }

    @Test
    void testGetByIdSuccess() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(new UserResponse());

        UserResponse response = userService.getById(1L);
        assertNotNull(response);
    }

    @Test
    void testGetByIdNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.getById(1L));
    }

    @Test
    void testCreate() {
        UserRequest request = new UserRequest();
        User entity = new User();
        UserResponse response = new UserResponse();

        when(userMapper.toEntity(request)).thenReturn(entity);
        when(userRepository.save(entity)).thenReturn(entity);
        when(userMapper.toResponse(entity)).thenReturn(response);

        UserResponse result = userService.create(request);
        assertEquals(response, result);
    }

    @Test
    void testUpdateSuccess() {
        UserRequest request = new UserRequest();
        request.setEmail("test@example.com");

        User existing = new User();
        User updated = new User();
        UserResponse response = new UserResponse();

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userMapper.toEntity(request)).thenReturn(updated);
        when(userRepository.save(any())).thenReturn(updated);
        when(userMapper.toResponse(updated)).thenReturn(response);

        UserResponse result = userService.update(1L, request);
        assertEquals(response, result);
    }

    @Test
    void testUpdateNotFound() {
        UserRequest request = new UserRequest();
        request.setEmail("test@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.update(1L, request));
    }

    @Test
    void testDeleteSuccess() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.delete(1L));
        verify(userRepository).delete(user);
    }

    @Test
    void testDeleteNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.delete(1L));
    }
}

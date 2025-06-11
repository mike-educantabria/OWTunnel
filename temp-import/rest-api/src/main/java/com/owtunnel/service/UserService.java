package com.owtunnel.service;

import com.owtunnel.dto.request.UserRequest;
import com.owtunnel.dto.response.UserResponse;
import com.owtunnel.exception.EntityNotFoundException;
import com.owtunnel.mapper.UserMapper;
import com.owtunnel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponse> getAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public UserResponse getById(Long id) {
        return userMapper.toResponse(userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User", id))
        );
    }

    @Transactional
    public UserResponse create(UserRequest request) {
        var user = userMapper.toEntity(request);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        var saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }

    @Transactional
    public UserResponse update(Long id, UserRequest request) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setEmail(request.getEmail());

                    if (request.getPassword() != null && !request.getPassword().isBlank()) {
                        String hashedPassword = passwordEncoder.encode(request.getPassword());
                        existingUser.setPasswordHash(hashedPassword);
                    }

                    existingUser.setFirstName(request.getFirstName());
                    existingUser.setLastName(request.getLastName());
                    existingUser.setLocale(request.getLocale());
                    existingUser.setRole(userMapper.mapStringToRole(request.getRole()));
                    return userMapper.toResponse(userRepository.save(existingUser));
                })
                .orElseThrow(() -> new EntityNotFoundException("User", id));
    }

    @Transactional
    public void delete(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User", id));
        userRepository.delete(user);
    }

    public UserResponse getByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("User", email));
    }
}

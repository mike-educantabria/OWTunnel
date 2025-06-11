package com.owtunnel.controller;

import com.owtunnel.dto.request.RegisterRequest;
import com.owtunnel.dto.request.UserRequest;
import com.owtunnel.dto.response.LoginResponse;
import com.owtunnel.dto.response.UserResponse;
import com.owtunnel.security.JwtUtils;
import com.owtunnel.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Tag(name = "Authentication")
@RestController
@RequestMapping("/auth")
public class RegisterController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@Valid @RequestBody RegisterRequest request) {
        var userRequest = new UserRequest();
        userRequest.setEmail(request.getEmail());
        userRequest.setPassword(request.getPassword());
        UserResponse userResponse = userService.create(userRequest);

        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        String token = jwtUtils.generateToken(auth.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(new LoginResponse(token, userResponse));
    }
}

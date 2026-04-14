package com.bookvault.bookvault.controller;

import com.bookvault.bookvault.dto.AuthRequest;
import com.bookvault.bookvault.dto.ResponseDTO;
import com.bookvault.bookvault.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseDTO login(@RequestBody AuthRequest request) {
        return authService.login(request.getUsername(), request.getPassword());
    }
}

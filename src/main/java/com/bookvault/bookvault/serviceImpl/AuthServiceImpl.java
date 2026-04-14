package com.bookvault.bookvault.serviceImpl;

import com.bookvault.bookvault.dto.ResponseDTO;
import com.bookvault.bookvault.entity.User;
import com.bookvault.bookvault.repository.UserRepository;
import com.bookvault.bookvault.service.AuthService;
import com.bookvault.bookvault.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public ResponseDTO login(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return new ResponseDTO("ERROR", "Username and password are required", null);
        }

        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return new ResponseDTO("ERROR", "Invalid username or password", null);
        }

        String token = jwtUtil.generateToken(user);

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("tokenType", "Bearer");
        data.put("role", user.getRole().name());

        return new ResponseDTO("SUCCESS", "Login successful", data);
    }
}

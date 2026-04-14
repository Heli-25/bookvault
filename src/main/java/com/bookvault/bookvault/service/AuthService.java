package com.bookvault.bookvault.service;

import com.bookvault.bookvault.dto.ResponseDTO;

public interface AuthService {
    ResponseDTO login(String username, String password);
}

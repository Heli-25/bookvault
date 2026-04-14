package com.bookvault.bookvault.service;

import com.bookvault.bookvault.dto.ResponseDTO;

public interface MemberService {
    ResponseDTO searchMembers(String query);
}

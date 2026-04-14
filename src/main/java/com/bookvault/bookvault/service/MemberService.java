package com.bookvault.bookvault.service;

import com.bookvault.bookvault.dto.MemberDTO;
import com.bookvault.bookvault.dto.ResponseDTO;

public interface MemberService {
    ResponseDTO getMembers();

    ResponseDTO getMemberById(String memberId);

    ResponseDTO createMember(MemberDTO dto);

    ResponseDTO updateMember(String memberId, MemberDTO dto);

    ResponseDTO deleteMember(String memberId);

    ResponseDTO searchMembers(String query);
}

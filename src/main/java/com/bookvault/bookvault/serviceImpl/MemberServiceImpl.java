package com.bookvault.bookvault.serviceImpl;

import com.bookvault.bookvault.dto.ResponseDTO;
import com.bookvault.bookvault.entity.Member;
import com.bookvault.bookvault.repository.MemberRepository;
import com.bookvault.bookvault.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public ResponseDTO searchMembers(String query) {
        if (query == null || query.isBlank()) {
            return new ResponseDTO("SUCCESS", "Members fetched successfully", Collections.emptyList());
        }

        String term = query.trim();
        List<Member> members = memberRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(term, term);

        return new ResponseDTO("SUCCESS", "Members fetched successfully", members);
    }
}

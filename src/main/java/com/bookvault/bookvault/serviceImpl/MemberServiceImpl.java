package com.bookvault.bookvault.serviceImpl;

import com.bookvault.bookvault.dto.MemberDTO;
import com.bookvault.bookvault.dto.ResponseDTO;
import com.bookvault.bookvault.entity.Member;
import com.bookvault.bookvault.enums.MembershipStatus;
import com.bookvault.bookvault.exception.MemberNotFoundException;
import com.bookvault.bookvault.repository.MemberRepository;
import com.bookvault.bookvault.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public ResponseDTO getMembers() {
        return ResponseDTO.success("Members fetched successfully", memberRepository.findAll());
    }

    @Override
    public ResponseDTO getMemberById(String memberId) {
        Member member = memberRepository.findById(UUID.fromString(memberId))
                .orElseThrow(() -> new MemberNotFoundException("MEMBER_NOT_FOUND"));

        return ResponseDTO.success("Member fetched successfully", member);
    }

    @Override
    public ResponseDTO createMember(MemberDTO dto) {
        Member member = new Member();
        member.setEmail(dto.getEmail());
        member.setName(dto.getName());
        member.setMembershipStatus(dto.getMembershipStatus() == null ? MembershipStatus.ACTIVE : dto.getMembershipStatus());

        return ResponseDTO.success("Member created successfully", memberRepository.save(member));
    }

    @Override
    public ResponseDTO updateMember(String memberId, MemberDTO dto) {
        Member member = memberRepository.findById(UUID.fromString(memberId))
                .orElseThrow(() -> new MemberNotFoundException("MEMBER_NOT_FOUND"));

        member.setEmail(dto.getEmail());
        member.setName(dto.getName());
        if (dto.getMembershipStatus() != null) {
            member.setMembershipStatus(dto.getMembershipStatus());
        }

        return ResponseDTO.success("Member updated successfully", memberRepository.save(member));
    }

    @Override
    public ResponseDTO deleteMember(String memberId) {
        Member member = memberRepository.findById(UUID.fromString(memberId))
                .orElseThrow(() -> new MemberNotFoundException("MEMBER_NOT_FOUND"));
        memberRepository.delete(member);
        return ResponseDTO.success("Member deleted successfully", memberId);
    }

    @Override
    public ResponseDTO searchMembers(String query) {
        if (query == null || query.isBlank()) {
            return ResponseDTO.success("Members fetched successfully", Collections.emptyList());
        }

        String term = query.trim();
        List<Member> members = memberRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(term, term);

        return ResponseDTO.success("Members fetched successfully", members);
    }
}

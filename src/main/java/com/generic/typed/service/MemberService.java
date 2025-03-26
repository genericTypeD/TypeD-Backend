package com.generic.typed.service;

import com.generic.typed.domain.Member;
import com.generic.typed.domain.Role;
import com.generic.typed.repository.MemberRepository;
import com.generic.typed.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public Member findByEmail(String email){
        return memberRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
    }

    @Transactional
    public Member addMember(Member member) {

        if (memberRepository.findByEmail(member.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        if (memberRepository.findByNickname(member.getNickname()).isPresent()) {
            throw new IllegalArgumentException("이미 사용중인 사용자 이름입니다.");
        }
        Optional<Role> userRoleOpt = roleRepository.findByName("ROLE_USER");
        Role userRole;

        if (userRoleOpt.isPresent()) {
            userRole = userRoleOpt.get();
        } else {
            // Role이 없으면 새로 생성
            userRole = new Role();
            userRole.setRoleId(1L);
            userRole.setName("ROLE_USER");
            userRole = roleRepository.save(userRole);
        }

        member.addRole(userRole);
        Member saveMember = memberRepository.save(member);
        return saveMember;
    }


    @Transactional(readOnly = true)
    public Optional<Member> getMember(Long memberId){
        return memberRepository.findById(memberId);
    }

    @Transactional(readOnly = true)
    public Optional<Member> getMember(String email){
        return memberRepository.findByEmail(email);
    }
}

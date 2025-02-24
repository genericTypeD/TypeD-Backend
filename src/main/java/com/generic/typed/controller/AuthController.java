package com.generic.typed.controller;

import com.generic.typed.domain.Member;
import com.generic.typed.domain.RefreshToken;
import com.generic.typed.dto.request.MemberLoginRequest;
import com.generic.typed.dto.request.MemberSignupRequest;
import com.generic.typed.dto.request.RefreshTokenRequest;
import com.generic.typed.dto.response.MemberLoginResponse;
import com.generic.typed.dto.response.MemberSignupResponse;
import com.generic.typed.security.jwt.util.JwtTokenizer;
import com.generic.typed.service.MemberService;
import com.generic.typed.service.RefreshTokenService;
import com.generic.typed.domain.Role;

import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final JwtTokenizer jwtTokenizer;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody @Valid MemberSignupRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        Member member = new Member();
        member.setNickname(request.getNickname());
        member.setEmail(request.getEmail());
        member.setPassword(passwordEncoder.encode(request.getPassword()));

        Member saveMember = memberService.addMember(member);

        MemberSignupResponse response = new MemberSignupResponse();
        response.setMemberId(saveMember.getId());
        response.setNickname(saveMember.getNickname());
        response.setCreatedAt(saveMember.getCreatedAt());
        response.setEmail(saveMember.getEmail());

        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid MemberLoginRequest login, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Member member = memberService.findByEmail(login.getEmail());
        if(!passwordEncoder.matches(login.getPassword(), member.getPassword())) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        List<String> roles = member.getRoles().stream().map(Role::getName).collect(Collectors.toList());

        // JWT 토큰 생성
        String accessToken = jwtTokenizer.createAccessToken(member.getId(),member.getEmail(),member.getNickname(), roles);
        String refreshToken = jwtTokenizer.createRefreshToken(member.getId(),member.getEmail(),member.getNickname(), roles);

        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setValue(refreshToken);
        refreshTokenEntity.setMemberId(member.getId());
        refreshTokenService.addRefreshToken(refreshTokenEntity);

        MemberLoginResponse loginResponse = MemberLoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .memberId(member.getId())
                .nickname(member.getNickname())
                .build();
        return new ResponseEntity(loginResponse, HttpStatus.OK);
    }

    @DeleteMapping("/logout")
    public ResponseEntity logout(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return new ResponseEntity(HttpStatus.OK);
    }
    @PostMapping("/refreshToken")
    public ResponseEntity requestRefresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        RefreshToken refreshToken = refreshTokenService.findRefreshToken(refreshTokenRequest.getRefreshToken()).orElseThrow(() -> new IllegalArgumentException("Refresh token 값을 찾을 수 없습니다"));
        Claims claims = jwtTokenizer.parseRefreshToken(refreshToken.getValue());

        Long memberId = Long.valueOf((Integer)claims.get("memberId"));

        Member member = memberService.getMember(memberId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다"));


        List roles = (List) claims.get("roles");
        String email = claims.getSubject();

        String accessToken = jwtTokenizer.createAccessToken(memberId, email, member.getNickname(), roles);

        MemberLoginResponse loginResponse = MemberLoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .memberId(member.getId())
                .nickname(member.getNickname())
                .build();
        return new ResponseEntity(loginResponse, HttpStatus.OK);
    }
}
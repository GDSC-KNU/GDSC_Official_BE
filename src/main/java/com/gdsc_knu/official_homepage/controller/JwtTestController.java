package com.gdsc_knu.official_homepage.controller;

import com.gdsc_knu.official_homepage.annotation.TokenMember;
import com.gdsc_knu.official_homepage.authentication.jwt.JwtMemberDetail;
import com.gdsc_knu.official_homepage.authentication.jwt.JwtProvider;
import com.gdsc_knu.official_homepage.authentication.jwt.JwtValidator;
import com.gdsc_knu.official_homepage.dto.jwt.TokenResponse;
import com.gdsc_knu.official_homepage.entity.enumeration.Role;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RequestMapping("/api/jwt")
@RestController
@RequiredArgsConstructor
public class JwtTestController {
    private final JwtProvider jwtProvider;
    private final JwtValidator jwtValidator;

    // chaejm55@gmail.com으로 jwt 토큰을 발급하는 테스트 컨트롤러입니다.
    // 현재 서버에 chaejm55@gmail.com 이메일 멤버가 입력되어있습니다.
    @GetMapping
    public ResponseEntity<TokenResponse> getJwtToken() {
        return ResponseEntity.ok().body(jwtProvider.issueTokens(1L, "chaejm55@gmail.com", Role.ROLE_CORE));
    }

    // 전달 받은 엑세스 토큰으로 멤버 MemberDetail(이메일)을 반환하는 테스트 컨트롤러입니다.
    @GetMapping("/check")
    public ResponseEntity<JwtMemberDetail> checkJwtToken(@TokenMember JwtMemberDetail jwtMemberDetail) {
        return ResponseEntity.ok().body(jwtMemberDetail);
    }

    // 리프레시 토큰으로 모든 토큰을 재발급 받는 테스트 컨트롤러입니다.
    @GetMapping("/reissue")
    public ResponseEntity<TokenResponse> reissueTokens(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(jwtProvider.reissueTokens(token));
    }

    // 권한 부여를 확인하기 위한 컨트롤러입니다.
    @GetMapping("/member")
    public String member(){
        return "member 권한을 가집니다.";
    }
    @GetMapping("/core")
    public String core(){
        return "core 권한을 가집니다.";
    }


}

package com.gdsc_knu.official_homepage.controller;

import com.gdsc_knu.official_homepage.dto.oauth.AuthorizationCode;
import com.gdsc_knu.official_homepage.dto.oauth.LoginResponseDto;
import com.gdsc_knu.official_homepage.oauth.OAuthService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@Tag(name = "Auth", description = "회원 인증(가입/로그인)관련 API")
@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final OAuthService oAuthService;

    /**
     * 로컬 환경에서 google oauth 테스트를 위함.
     */
    @Hidden
    @GetMapping("google/oauth")
    public ResponseEntity<LoginResponseDto> googleOAuth(@RequestParam(name="code")String code){
        return ResponseEntity.ok().body(oAuthService.getGoogleAccessToken(code));
    }

    @PostMapping("/oauth")
    public ResponseEntity<LoginResponseDto> googleOAuth(@RequestBody AuthorizationCode code){
        return ResponseEntity.ok().body(oAuthService.getGoogleAccessToken(code.getCode()));
    }

}

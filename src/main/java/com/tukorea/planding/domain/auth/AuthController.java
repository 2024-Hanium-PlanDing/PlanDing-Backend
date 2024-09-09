package com.tukorea.planding.domain.auth;

import com.tukorea.planding.common.CommonResponse;
import com.tukorea.planding.common.CommonUtils;
import com.tukorea.planding.domain.auth.dto.TemporaryTokenRequest;
import com.tukorea.planding.domain.auth.dto.TemporaryTokenResponse;
import com.tukorea.planding.domain.auth.dto.TokenResponse;
import com.tukorea.planding.domain.auth.service.TokenService;
import com.tukorea.planding.global.config.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/temporary-token")
public class AuthController {
    private final TokenService tokenService;
    private final JwtUtil jwtUtil;

    @PostMapping()
    public CommonResponse<?> issueToken(@RequestBody TemporaryTokenRequest tokenRequest, HttpServletResponse response){
        TokenResponse tokenResponse = tokenService.issueTokenFromTemporaryToken(tokenRequest.temporaryToken());
        jwtUtil.sendAccessAndRefreshToken(response, tokenResponse.accessToken(), tokenResponse.refreshToken());
        return CommonUtils.successWithEmptyData();
    }
}

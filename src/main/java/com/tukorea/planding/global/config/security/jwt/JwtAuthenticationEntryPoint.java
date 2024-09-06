package com.tukorea.planding.global.config.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tukorea.planding.global.error.ErrorCode;
import com.tukorea.planding.global.error.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final JwtTokenHandler jwtTokenHandler;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        String accessToken = jwtTokenHandler.extractAccessToken(request).orElse(null);

        if (accessToken == null) {
            // 토큰이 없을때
            ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INVALID_AUTH_TOKEN);
            sendErrorResponse(response, errorResponse);
        }

        // 토큰 만료시
        if (accessToken != null && !jwtTokenHandler.validateToken(accessToken)) {
            ErrorResponse errorResponse = new ErrorResponse(ErrorCode.EXPIRED_AUTH_TOKEN);
            sendErrorResponse(response, errorResponse);
        }
    }

    private void sendErrorResponse(HttpServletResponse response, ErrorResponse errorResponse) throws IOException {
        response.setStatus(errorResponse.getStatus().value());
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(errorResponse);

        response.getWriter().write(result);
    }
}

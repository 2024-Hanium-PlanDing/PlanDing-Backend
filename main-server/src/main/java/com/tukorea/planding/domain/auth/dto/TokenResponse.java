package com.tukorea.planding.domain.auth.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken) {

}

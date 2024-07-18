package com.tukorea.planding.global.config.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public enum JwtConstant {
    BEARER("Bearer ");

    private String value;
}

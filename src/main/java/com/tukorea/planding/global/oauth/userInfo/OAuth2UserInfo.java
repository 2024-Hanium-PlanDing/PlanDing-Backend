package com.tukorea.planding.global.oauth.userInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes;
    public abstract String getOAuth2Id();
    public abstract String getNickname();
    public abstract String getProfileImage();
    public abstract String getEmail();

}

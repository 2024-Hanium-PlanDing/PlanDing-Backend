package com.tukorea.planding.global.oauth.service;

import com.tukorea.planding.domain.notify.entity.UserNotificationSetting;
import com.tukorea.planding.domain.notify.repository.setting.UserNotificationSettingRepository;
import com.tukorea.planding.domain.user.repository.UserRepository;
import com.tukorea.planding.domain.user.entity.SocialType;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final UserService userService;
    private final UserNotificationSettingRepository userNotificationSettingRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialType socialType = SocialType.getSocialType(registrationId);
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); // OAuth2 로그인 시 키(PK)가 되는 값

        Map<String, Object> attributes = oAuth2User.getAttributes(); // 소셜 로그인에서 API가 제공하는 userInfo의 Json 값(유저 정보들)

        OAuthAttributes extractAttributes = OAuthAttributes.of(socialType, userNameAttributeName, attributes);

        User createdUser = getUser(extractAttributes, socialType);

        log.info("[" + registrationId + "]:OAuth 객체 생성");
        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(createdUser.getRole().getAuthority())),
                attributes,
                extractAttributes.getNameAttributeKey(),
                createdUser.getId(),
                createdUser.getEmail(),
                createdUser.getRole(),
                createdUser.getUserCode()
        );

    }

    private User getUser(OAuthAttributes attributes, SocialType socialType) {
        User findUser = userRepository.findBySocialTypeAndSocialId(socialType,
                attributes.getOauth2UserInfo().getOAuth2Id()).orElse(null);

        if (findUser == null) {
            return saveUser(attributes, socialType);
        }

        log.info("기존 유저 유저 [userCode = " + findUser.getUserCode() + "]");
        return findUser;
    }

    private User saveUser(OAuthAttributes attributes, SocialType socialType) {
        log.info("신규 회원가입");
        String userCode = userService.generateUniqueUserCode();
        User createdUser = attributes.toEntity(socialType, attributes.getOauth2UserInfo(), userCode);
        UserNotificationSetting notificationSetting = attributes.toNotificationSetting(createdUser);
        userNotificationSettingRepository.save(notificationSetting);
        return userRepository.save(createdUser);
    }

}

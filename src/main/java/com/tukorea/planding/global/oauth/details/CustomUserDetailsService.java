package com.tukorea.planding.global.oauth.details;

import com.tukorea.planding.domain.user.entity.UserDomain;
import com.tukorea.planding.domain.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserQueryService userQueryService;

    @Override
    public UserDetails loadUserByUsername(String userCode) throws UsernameNotFoundException {
        UserDomain user = userQueryService.getUserByUserCode(userCode);
        return new CustomUser(user.getUsername(), user.getEmail(), user.getRole());
    }
}

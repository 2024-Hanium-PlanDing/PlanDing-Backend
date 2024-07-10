package com.tukorea.planding.global.oauth.details;

import com.tukorea.planding.domain.user.repository.UserRepository;
import com.tukorea.planding.domain.user.entity.User;
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

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userCode) throws UsernameNotFoundException {
        User user = userRepository.findByUserCode(userCode)
                .orElseThrow(() -> new RuntimeException("존재하지않는 유저입니다."));
        return new CustomUser(user.getUsername(), user.getEmail() ,user.getRole());
    }
}

package com.tukorea.planding.domain.user.service;

import com.tukorea.planding.common.service.UserCodeHolder;
import com.tukorea.planding.domain.user.service.port.UserCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCodeGeneratorImpl implements UserCodeGenerator {
    private final UserQueryService userQueryService;
    private final UserCodeHolder userCodeHolder;

    @Override
    public String generateUniqueUserCode() {
        String userCode;
        do {
            userCode = userCodeHolder.userCode();
        } while (userQueryService.existsByUserCode(userCode));
        return userCode;
    }
}

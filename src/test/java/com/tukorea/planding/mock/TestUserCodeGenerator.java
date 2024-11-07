package com.tukorea.planding.mock;

import com.tukorea.planding.domain.user.service.port.UserCodeGenerator;

public class TestUserCodeGenerator implements UserCodeGenerator {

    private int count = 1;

    @Override
    public String generateUniqueUserCode() {
        return String.valueOf(count++);
    }
}

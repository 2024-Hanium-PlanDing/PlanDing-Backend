package com.tukorea.planding.mock;

import com.tukorea.planding.common.service.UserCodeHolder;
public class TestUserCodeHolder implements UserCodeHolder {

    private final String userCode;

    public TestUserCodeHolder(String userCode) {
        this.userCode = userCode;
    }

    @Override
    public String userCode() {
        return userCode;
    }
}

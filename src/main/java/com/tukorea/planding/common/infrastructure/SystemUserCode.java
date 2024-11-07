package com.tukorea.planding.common.infrastructure;

import com.tukorea.planding.common.service.UserCodeHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SystemUserCode implements UserCodeHolder {
    @Override
    public String userCode() {
        return "#" + UUID.randomUUID().toString().substring(0, 4);
    }
}

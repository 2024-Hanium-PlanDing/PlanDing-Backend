package com.tukorea.planding.common.infrastructure;

import com.tukorea.planding.common.service.GroupRoomCodeHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Component
public class SystemGroupCode implements GroupRoomCodeHolder {
    @Override
    public String groupCode() {
        return "G" + UUID.randomUUID();
    }
}

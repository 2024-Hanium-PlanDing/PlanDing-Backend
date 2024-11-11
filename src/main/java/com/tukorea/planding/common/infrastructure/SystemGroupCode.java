package com.tukorea.planding.common.infrastructure;

import com.tukorea.planding.common.service.GroupRoomCodeHolder;

import java.util.UUID;

public class SystemGroupCode implements GroupRoomCodeHolder {
    @Override
    public String groupCode() {
        return "G" + UUID.randomUUID();
    }
}

package com.tukorea.planding.mock;

import com.tukorea.planding.common.service.GroupRoomCodeHolder;

public class TestGroupCodeHolder implements GroupRoomCodeHolder {
    @Override
    public String groupCode() {
        return "G1234";
    }
}

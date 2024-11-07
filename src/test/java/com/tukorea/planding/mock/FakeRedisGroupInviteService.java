package com.tukorea.planding.mock;

import com.tukorea.planding.domain.group.dto.response.GroupInviteMessageResponse;
import com.tukorea.planding.domain.group.service.port.RedisGroupInviteService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeRedisGroupInviteService implements RedisGroupInviteService {
    private final Map<String, Map<String, GroupInviteMessageResponse>> invitations = new HashMap<>();

    @Override
    public void createInvitation(String userCode, GroupInviteMessageResponse inviteDTO) {
        String inviteCode = inviteDTO.getInviteCode();
        invitations
                .computeIfAbsent(userCode, k -> new HashMap<>())
                .put(inviteCode, inviteDTO);
    }

    @Override
    public List<GroupInviteMessageResponse> getAllInvitations(String userCode) {
        return new ArrayList<>(invitations.getOrDefault(userCode, new HashMap<>()).values());
    }

    @Override
    public void deleteInvitation(String userCode, String inviteCode) {
        Map<String, GroupInviteMessageResponse> userInvitations = invitations.get(userCode);
        if (userInvitations != null) {
            userInvitations.remove(inviteCode);
        }
    }
}
package com.tukorea.planding.domain.group.service.port;

import com.tukorea.planding.domain.group.dto.response.GroupInviteMessageResponse;

import java.util.List;

public interface RedisGroupInviteService {
    void createInvitation(String userCode, GroupInviteMessageResponse inviteDTO);

    List<GroupInviteMessageResponse> getAllInvitations(String userCode);

    void deleteInvitation(String userCode, String inviteCode);

}

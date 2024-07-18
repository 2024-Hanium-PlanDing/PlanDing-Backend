package com.tukorea.planding.domain.group.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class GroupInviteMessageResponse {
    private String inviteCode;
    private Long groupRoomId;
    private String invitedUserCode;
    private Long invitingUserId;

    public static GroupInviteMessageResponse create(String inviteCode, Long groupRoomId, String invitedUserCode, Long invitingUserId) {
        return new GroupInviteMessageResponse(inviteCode, groupRoomId, invitedUserCode, invitingUserId);
    }
}

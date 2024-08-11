package com.tukorea.planding.domain.group.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class GroupInviteMessageResponse {
    private String inviteCode;
    private String groupCod;
    private String groupName;
    private String invitedUserCode;
    private Long invitingUserId;
    private LocalDateTime createdAt;

    public static GroupInviteMessageResponse create(String inviteCode, String groupCode, String groupName, String invitedUserCode, Long invitingUserId, LocalDateTime createdAt) {
        return new GroupInviteMessageResponse(inviteCode, groupCode, groupName, invitedUserCode, invitingUserId, createdAt);
    }
}

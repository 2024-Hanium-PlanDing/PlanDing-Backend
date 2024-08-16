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
    private String groupCode;
    private String groupName;
    private String invitedUserCode;
    private LocalDateTime createdAt;

    public static GroupInviteMessageResponse create(String inviteCode, String groupCode, String groupName, String invitedUserCode, LocalDateTime createdAt) {
        return new GroupInviteMessageResponse(inviteCode, groupCode, groupName, invitedUserCode, createdAt);
    }
}

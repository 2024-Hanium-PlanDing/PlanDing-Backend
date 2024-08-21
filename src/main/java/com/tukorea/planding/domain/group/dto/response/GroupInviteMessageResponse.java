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
    private String userName;
    private GroupResponse groupResponse;
    private LocalDateTime createdAt;

    public static GroupInviteMessageResponse create(String inviteCode, String groupCode, String groupName, String invitedUserCode, String userName, GroupResponse response, LocalDateTime createdAt) {
        return new GroupInviteMessageResponse(inviteCode, groupCode, groupName, invitedUserCode, userName, response, createdAt);
    }
}

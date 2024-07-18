package com.tukorea.planding.domain.group.controller;

import com.tukorea.planding.common.CommonResponse;
import com.tukorea.planding.common.CommonUtils;
import com.tukorea.planding.domain.chat.service.ChatRoomFacadeService;
import com.tukorea.planding.domain.group.dto.request.GroupInviteRequest;
import com.tukorea.planding.domain.group.dto.response.GroupInviteAcceptResponse;
import com.tukorea.planding.domain.group.dto.response.GroupInviteMessageResponse;
import com.tukorea.planding.domain.group.service.GroupInviteService;
import com.tukorea.planding.domain.user.dto.UserInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Group Invite", description = "그룹 초대 관련")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/invitation")
public class GroupInviteController {
    private final GroupInviteService groupInviteService;
    private final ChatRoomFacadeService chatRoomFacadeService;

    @Operation(summary = "유저에게 초대를 보낸다")
    @PostMapping()
    public CommonResponse<GroupInviteMessageResponse> invite(@AuthenticationPrincipal UserInfo userInfo, @RequestBody GroupInviteRequest groupInviteRequest) {
        return CommonUtils.success(groupInviteService.inviteGroupRoom(userInfo, groupInviteRequest));
    }

    @Operation(summary = "초대를 수락한다")
    @GetMapping("/accept/{groupCode}/{inviteCode}")
    public CommonResponse<GroupInviteAcceptResponse> accept(@AuthenticationPrincipal UserInfo userInfo, @PathVariable(name = "groupCode") String groupCode, @PathVariable(name = "inviteCode") String code) {
        GroupInviteAcceptResponse response = chatRoomFacadeService.acceptInvitationAndChatRoom(userInfo, code, groupCode);
        return CommonUtils.success(response);
    }

    @Operation(summary = "초대를 받은 목록", description = "아직 초대의 상태를 바꾸지 않은 경우만")
    @GetMapping()
    public CommonResponse<List<GroupInviteMessageResponse>> getInvitations(@AuthenticationPrincipal UserInfo userInfo) {
        List<GroupInviteMessageResponse> groupInviteResponse = groupInviteService.getInvitations(userInfo);
        return CommonUtils.success(groupInviteResponse);
    }

    @Operation(summary = "초대를 거절한다.")
    @DeleteMapping("/decline/{inviteCode}")
    public CommonResponse<?> declineInvitation(@AuthenticationPrincipal UserInfo userInfo, @PathVariable(name = "inviteCode") String code) {
        groupInviteService.declineInvitation(userInfo, code);
        return CommonUtils.success("거절하였습니다.");
    }
}

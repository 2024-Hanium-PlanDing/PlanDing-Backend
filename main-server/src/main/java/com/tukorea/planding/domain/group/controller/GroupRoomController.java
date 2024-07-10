package com.tukorea.planding.domain.group.controller;

import com.tukorea.planding.common.CommonResponse;
import com.tukorea.planding.common.CommonUtils;
import com.tukorea.planding.domain.chat.service.ChatRoomFacadeService;
import com.tukorea.planding.domain.group.dto.request.GroupCreateRequest;
import com.tukorea.planding.domain.group.dto.request.GroupUpdateRequest;
import com.tukorea.planding.domain.group.dto.response.GroupResponse;
import com.tukorea.planding.domain.group.dto.response.GroupUserResponse;
import com.tukorea.planding.domain.group.service.GroupRoomService;
import com.tukorea.planding.domain.user.dto.UserInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "GroupRoom", description = "다른 유저와 함께 스케줄을 같이 관리할 그룹 만들기")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/group")
public class GroupRoomController {

    private final GroupRoomService groupRoomService;
    private final ChatRoomFacadeService chatRoomFacadeService;

    @Operation(summary = "메인페이지 API", description = "내 그룹 가져오기")
    @GetMapping()
    public CommonResponse<List<GroupResponse>> getAllGroupRoomByUser(@AuthenticationPrincipal UserInfo userInfo) {
        List<GroupResponse> groupResponses = groupRoomService.getAllGroupRoomByUser(userInfo);
        return CommonUtils.success(groupResponses);
    }

    @Operation(summary = "그룹 정보 조회")
    @GetMapping("/{groupCode}")
    public CommonResponse<GroupUserResponse> getUserByGroup(@AuthenticationPrincipal UserInfo userInfo, @PathVariable String groupCode) {
        GroupUserResponse responses = groupRoomService.getGroupUsers(userInfo, groupCode);
        return CommonUtils.success(responses);
    }

    @Operation(summary = "그룹 생성")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse<GroupResponse> createGroupRoom(@AuthenticationPrincipal UserInfo userInfo,
                                                         @RequestPart(value = "request") GroupCreateRequest createGroupRoom,
                                                         @RequestPart(value = "thumbnail") MultipartFile thumbnailFile) {
        GroupResponse groupResponse = chatRoomFacadeService.createGroupRoomWithChat(userInfo, createGroupRoom, thumbnailFile);
        return CommonUtils.success(groupResponse);
    }

    @Operation(summary = "그룹 정보 수정")
    @PatchMapping()
    public CommonResponse<GroupResponse> updateGroupNameOrDescription(@AuthenticationPrincipal UserInfo userInfo, @RequestBody GroupUpdateRequest groupUpdateRequest) {
        GroupResponse groupResponses = groupRoomService.updateGroupNameOrDescription(userInfo, groupUpdateRequest);
        return CommonUtils.success(groupResponses);
    }

    @Operation(summary = "그룹 삭제")
    @DeleteMapping("/{groupCode}")
    public CommonResponse<?> deleteGroup(@AuthenticationPrincipal UserInfo userInfo, @PathVariable String groupCode) {
        groupRoomService.deleteGroup(userInfo, groupCode);
        return CommonUtils.success("그룹삭제 완료.");
    }

    @Operation(summary = "그룹 나가기")
    @DeleteMapping("/leave/{groupCode}")
    public CommonResponse<?> leaveGroup(@AuthenticationPrincipal UserInfo userInfo, @PathVariable String groupCode) {
        chatRoomFacadeService.leaveGroup(userInfo, groupCode);
        return CommonUtils.success("그룹 나가기 완료.");
    }
}

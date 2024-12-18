package com.tukorea.planding.domain.group.controller;

import com.tukorea.planding.common.CommonResponse;
import com.tukorea.planding.common.CommonUtils;
import com.tukorea.planding.domain.group.dto.request.GroupCreateRequest;
import com.tukorea.planding.domain.group.dto.request.GroupUpdateRequest;
import com.tukorea.planding.domain.group.dto.response.GroupInformationResponse;
import com.tukorea.planding.domain.group.dto.response.GroupResponse;
import com.tukorea.planding.domain.group.service.GroupRoomService;
import com.tukorea.planding.domain.user.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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

    @Operation(summary = "메인페이지 API", description = "내 그룹 가져오기")
    @GetMapping("/paging")
    public CommonResponse<List<GroupResponse>> getAllGroupRoomByUser(@AuthenticationPrincipal UserResponse userResponse,
                                                                     @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "11") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        List<GroupResponse> groupResponses = groupRoomService.getAllGroupRoomByUser(userResponse, pageRequest);
        return CommonUtils.success(groupResponses);
    }

    @Operation(summary = "그룹 정보 조회")
    @GetMapping("/{groupCode}")
    public CommonResponse<GroupInformationResponse> getUserByGroup(@AuthenticationPrincipal UserResponse userResponse, @PathVariable String groupCode) {
        GroupInformationResponse responses = groupRoomService.getGroupUsers(userResponse, groupCode);
        return CommonUtils.success(responses);
    }

    @Operation(summary = "그룹 생성")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse<GroupResponse> createGroupRoom(@AuthenticationPrincipal UserResponse userResponse,
                                                         @RequestPart(value = "request") GroupCreateRequest createGroupRoom,
                                                         @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnailFile) {
        GroupResponse groupResponse = groupRoomService.createGroupRoom(userResponse, createGroupRoom, thumbnailFile);
        return CommonUtils.success(groupResponse);
    }

    @Operation(summary = "그룹 정보 수정")
    @PatchMapping()
    public CommonResponse<GroupResponse> updateGroupNameOrDescription(@AuthenticationPrincipal UserResponse userResponse, @RequestBody GroupUpdateRequest groupUpdateRequest) {
        GroupResponse groupResponses = groupRoomService.updateGroupNameOrDescription(userResponse, groupUpdateRequest);
        return CommonUtils.success(groupResponses);
    }

    @Operation(summary = "그룹 삭제")
    @DeleteMapping("/{groupCode}")
    public CommonResponse<?> deleteGroup(@AuthenticationPrincipal UserResponse userResponse, @PathVariable String groupCode) {
        groupRoomService.deleteGroup(userResponse, groupCode);
        return CommonUtils.success("그룹삭제 완료.");
    }

    @Operation(summary = "그룹 나가기")
    @DeleteMapping("/leave/{groupCode}")
    public CommonResponse<?> leaveGroup(@AuthenticationPrincipal UserResponse userResponse, @PathVariable String groupCode) {
        groupRoomService.leaveGroup(userResponse, groupCode);
        return CommonUtils.success("그룹 나가기 완료.");
    }
}

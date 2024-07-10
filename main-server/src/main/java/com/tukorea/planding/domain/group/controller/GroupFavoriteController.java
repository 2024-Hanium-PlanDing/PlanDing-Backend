package com.tukorea.planding.domain.group.controller;

import com.tukorea.planding.common.CommonResponse;
import com.tukorea.planding.common.CommonUtils;
import com.tukorea.planding.domain.group.dto.response.GroupFavoriteResponse;
import com.tukorea.planding.domain.group.service.GroupFavoriteService;
import com.tukorea.planding.domain.user.dto.UserInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Group Favorite", description = "그룹 즐겨찾기 관련")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/favorite")
public class GroupFavoriteController {

    private final GroupFavoriteService groupFavoriteService;

    @Operation(summary = "그룹 즐겨찾기 추가")
    @GetMapping("/{groupCode}")
    public CommonResponse<GroupFavoriteResponse> addFavorite(@AuthenticationPrincipal UserInfo userInfo, @PathVariable String groupCode) {
        GroupFavoriteResponse response = groupFavoriteService.addFavorite(userInfo, groupCode);
        return CommonUtils.success(response);
    }

    @Operation(summary = "그룹 즐겨찾기 해제")
    @DeleteMapping("/{groupCode}")
    public CommonResponse<?> deleteFavorite(@AuthenticationPrincipal UserInfo userInfo, @PathVariable String groupCode) {
        groupFavoriteService.deleteFavorite(userInfo, groupCode);
        return CommonUtils.success("즐겨찾기 해제 완료.");
    }

}

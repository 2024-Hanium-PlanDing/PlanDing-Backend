package com.tukorea.planding.domain.group.controller;

import com.tukorea.planding.common.CommonResponse;
import com.tukorea.planding.common.CommonUtils;
import com.tukorea.planding.domain.group.dto.response.GroupFavoriteResponse;
import com.tukorea.planding.domain.group.dto.response.GroupResponse;
import com.tukorea.planding.domain.group.service.GroupFavoriteService;
import com.tukorea.planding.domain.user.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Group Favorite", description = "그룹 즐겨찾기 관련")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/favorite")
public class GroupFavoriteController {

    private final GroupFavoriteService groupFavoriteService;

    @Operation(summary = "즐겨찾기 그룹 조회")
    @GetMapping()
    public CommonResponse<List<GroupResponse>> searchFavorite(@AuthenticationPrincipal UserResponse userResponse) {
        return CommonUtils.success(groupFavoriteService.findFavoriteGroupsByUser(userResponse));
    }

    @Operation(summary = "그룹 즐겨찾기 추가")
    @PostMapping("/{groupCode}")
    public CommonResponse<GroupFavoriteResponse> addFavorite(@AuthenticationPrincipal UserResponse userResponse, @PathVariable String groupCode) {
        GroupFavoriteResponse response = groupFavoriteService.addFavorite(userResponse, groupCode);
        return CommonUtils.success(response);
    }

    @Operation(summary = "그룹 즐겨찾기 해제")
    @DeleteMapping("/{groupCode}")
    public CommonResponse<?> deleteFavorite(@AuthenticationPrincipal UserResponse userResponse, @PathVariable String groupCode) {
        groupFavoriteService.deleteFavorite(userResponse, groupCode);
        return CommonUtils.success("즐겨찾기 해제 완료.");
    }


}

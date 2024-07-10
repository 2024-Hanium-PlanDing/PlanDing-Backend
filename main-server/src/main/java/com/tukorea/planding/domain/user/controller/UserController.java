package com.tukorea.planding.domain.user.controller;

import com.tukorea.planding.common.CommonResponse;
import com.tukorea.planding.common.CommonUtils;
import com.tukorea.planding.domain.group.entity.GroupRoom;
import com.tukorea.planding.domain.user.dto.AndroidLoginRequest;
import com.tukorea.planding.domain.user.dto.AndroidLoginResponse;
import com.tukorea.planding.domain.user.dto.ProfileResponse;
import com.tukorea.planding.domain.user.dto.UserInfo;
import com.tukorea.planding.domain.user.service.AndroidLoginService;
import com.tukorea.planding.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User", description = "회원 API 문서")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final AndroidLoginService androidLoginService;
    private final UserService userService;

    @Operation(summary = "안드로이드 로그인")
    @PostMapping("/login/android/kakao")
    public CommonResponse<AndroidLoginResponse> signupApp(@RequestBody AndroidLoginRequest androidLoginRequest) {
        AndroidLoginResponse response = androidLoginService.signupApp(androidLoginRequest);
        return CommonUtils.success(response);
    }

    @Operation(summary = "프로필 가져오기", description = "즐겨찾는 그룹, 그룹요청")
    @GetMapping("/profile")
    public CommonResponse<ProfileResponse> getProfile(@AuthenticationPrincipal UserInfo userInfo) {
        ProfileResponse profile = userService.getProfile(userInfo);
        return CommonUtils.success(profile);
    }

    @Operation(summary = "즐겨찾기 그룹 조회")
    @GetMapping()
    public CommonResponse<List<GroupRoom>> searchFavorite(@AuthenticationPrincipal UserInfo userInfo) {
        return CommonUtils.success(userService.findFavoriteGroupsByUserId(userInfo));
    }
}

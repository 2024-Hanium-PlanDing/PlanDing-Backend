package com.tukorea.planding.domain.notify.service.setting;

import com.tukorea.planding.domain.notify.entity.UserNotificationSetting;
import com.tukorea.planding.domain.notify.repository.setting.UserNotificationSettingRepository;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserNotificationSettingQueryService {

    private final UserNotificationSettingRepository userNotificationSettingRepository;
    public UserNotificationSetting getSettingValue(String userCode) {
        return userNotificationSettingRepository.findByUserCode(userCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.SETTING_NOT_FOUND));
    }
}

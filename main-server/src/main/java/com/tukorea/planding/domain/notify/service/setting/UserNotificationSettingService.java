package com.tukorea.planding.domain.notify.service.setting;

import com.tukorea.planding.domain.notify.dto.NotificationSettingResponse;
import com.tukorea.planding.domain.notify.entity.UserNotificationSetting;
import com.tukorea.planding.domain.user.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserNotificationSettingService {

    private final UserNotificationSettingQueryService userNotificationSettingQueryService;

    public NotificationSettingResponse getNotificationSetting(UserInfo userInfo) {
        UserNotificationSetting setting = userNotificationSettingQueryService.getSettingValue(userInfo.getUserCode());
        return NotificationSettingResponse.builder()
                .personalSchedule(setting.isScheduleNotificationEnabled())
                .groupSchedule(setting.isGroupScheduleNotificationEnabled())
                .build();
    }

    public void updateScheduleNotificationSetting(String userCode, boolean enabled) {
        UserNotificationSetting setting = userNotificationSettingQueryService.getSettingValue(userCode);
        setting.updateScheduleNotificationEnabled(enabled);
    }

    public void updateGroupScheduleNotificationSetting(String userCode, boolean enabled) {
        UserNotificationSetting setting = userNotificationSettingQueryService.getSettingValue(userCode);
        setting.updateGroupScheduleNotificationEnabled(enabled);
    }
}

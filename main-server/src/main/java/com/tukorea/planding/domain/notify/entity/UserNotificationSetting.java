package com.tukorea.planding.domain.notify.entity;

import com.tukorea.planding.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class UserNotificationSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userCode;

    private boolean scheduleNotificationEnabled;

    private boolean groupScheduleNotificationEnabled;

    @Builder
    public UserNotificationSetting(String userCode, boolean scheduleNotificationEnabled, boolean groupScheduleNotificationEnabled) {
        this.userCode = userCode;
        this.scheduleNotificationEnabled = scheduleNotificationEnabled;
        this.groupScheduleNotificationEnabled = groupScheduleNotificationEnabled;
    }

    public void updateScheduleNotificationEnabled(boolean scheduleNotificationEnabled) {
        this.scheduleNotificationEnabled = scheduleNotificationEnabled;
    }

    public void updateGroupScheduleNotificationEnabled(boolean groupScheduleNotificationEnabled) {
        this.groupScheduleNotificationEnabled = groupScheduleNotificationEnabled;
    }
}

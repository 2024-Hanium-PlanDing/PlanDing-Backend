package com.tukorea.planding.domain.notify.repository.setting;

import com.tukorea.planding.domain.notify.entity.UserNotificationSetting;
import com.tukorea.planding.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserNotificationSettingRepository extends JpaRepository<UserNotificationSetting, Long> {
    Optional<UserNotificationSetting> findByUserCode(String userCode);
}

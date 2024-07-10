package com.tukorea.planding.domain.schedule.entity;

import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "PERSONAL_SCHEDULE")
public class PersonalSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "personal_schedule_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "personalSchedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Schedule> schedules = new HashSet<>();

    @Builder
    public PersonalSchedule(User user) {
        this.user = user;
    }

    public void checkOwnership(Long userId) throws BusinessException {
        if (!this.user.getId().equals(userId)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_SCHEDULE);
        }
    }
}

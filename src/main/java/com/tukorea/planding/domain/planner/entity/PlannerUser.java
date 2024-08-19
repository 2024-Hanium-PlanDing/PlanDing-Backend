package com.tukorea.planding.domain.planner.entity;

import com.tukorea.planding.domain.planner.PlannerRole;
import com.tukorea.planding.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlannerUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "planner_id", nullable = false)
    private Planner planner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private PlannerRole role;

    public void updateRole(PlannerRole newRole) {
        this.role = newRole;
    }
}

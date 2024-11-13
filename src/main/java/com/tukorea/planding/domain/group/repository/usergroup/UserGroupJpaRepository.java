package com.tukorea.planding.domain.group.repository.usergroup;

import com.tukorea.planding.domain.group.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupJpaRepository extends JpaRepository<UserGroup, Long>, UserGroupRepositoryCustom {
}

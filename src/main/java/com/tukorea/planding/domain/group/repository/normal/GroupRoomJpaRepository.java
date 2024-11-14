package com.tukorea.planding.domain.group.repository.normal;

import com.tukorea.planding.domain.group.entity.GroupRoom;
import com.tukorea.planding.domain.user.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GroupRoomJpaRepository extends JpaRepository<GroupRoom, Long> {
    Optional<GroupRoom> findByGroupCode(String groupCode);

    boolean existsByGroupCode(String groupCode);

    @Query("SELECT gr " +
            "FROM GroupRoom gr " +
            "JOIN gr.userGroups ug " +
            "WHERE ug.user.id = :userId " +
            "ORDER BY gr.createdDate ASC")
    List<GroupRoom> findGroupRoomsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT u " +
            "FROM User u " +
            "JOIN u.userGroup ug " +
            "WHERE ug.groupRoom.groupCode = :groupCode")
    List<User> getGroupUsers(@Param("groupCode") String groupCode);
}

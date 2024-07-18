package com.tukorea.planding.domain.group.repository.normal;

import com.tukorea.planding.domain.group.entity.GroupRoom;
import com.tukorea.planding.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupRoomRepository extends JpaRepository<GroupRoom, Long>, GroupRoomRepositoryCustom {
    Optional<GroupRoom> findByGroupCode(String groupCode);
    boolean existsById(Long groupRoomId);

}

package com.tukorea.planding.domain.group.repository.favorite;

import com.tukorea.planding.domain.group.entity.GroupFavorite;
import com.tukorea.planding.domain.group.repository.normal.GroupRoomRepositoryCustom;
import com.tukorea.planding.domain.user.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GroupFavoriteRepository extends JpaRepository<GroupFavorite, Long>, GroupFavoriteRepositoryCustom {

    @Modifying
    @Transactional
    @Query("DELETE FROM GroupFavorite gf WHERE gf.user.id = :userId AND gf.groupRoom.groupCode = :groupCode")
    void deleteByUserIdAndGroupRoomId(@Param("userId") Long userId, @Param("groupCode") String groupCode);
}

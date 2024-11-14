package com.tukorea.planding.domain.group.repository.favorite;

import com.tukorea.planding.domain.group.entity.GroupFavorite;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GroupFavoriteJpaRepository extends JpaRepository<GroupFavorite, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM GroupFavorite gf WHERE gf.user.id = :userId AND gf.groupRoom.groupCode = :groupCode")
    void deleteByUserIdAndGroupRoomId(@Param("userId") Long userId, @Param("groupCode") String groupCode);

    @Query("SELECT gf FROM GroupFavorite gf JOIN FETCH gf.groupRoom WHERE gf.user.id = :userId")
    List<GroupFavorite> findFavoriteGroupsByUser(Long userId);

    @Query("SELECT CASE WHEN COUNT(gf) > 0 THEN true ELSE false END " +
            "FROM GroupFavorite gf " +
            "JOIN gf.groupRoom gr " +
            "WHERE gf.user.userCode = :userCode AND gr.groupCode = :groupCode")
    Boolean existsByUserAndGroupRoom(@Param("userCode") String userCode, @Param("groupCode") String groupCode);
}

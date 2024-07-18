package com.tukorea.planding.domain.group.service.query;

import com.tukorea.planding.domain.group.repository.favorite.GroupFavoriteRepository;
import com.tukorea.planding.domain.group.repository.favorite.GroupFavoriteRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupFavoriteQueryService {

    private final GroupFavoriteRepository groupFavoriteRepository;

    public Long countMyFavoriteGroup(String userCode) {
        return groupFavoriteRepository.countMyFavoriteGroup(userCode);
    }
}

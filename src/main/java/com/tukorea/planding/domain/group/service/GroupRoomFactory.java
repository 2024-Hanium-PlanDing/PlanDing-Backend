package com.tukorea.planding.domain.group.service;

import com.tukorea.planding.domain.group.dto.request.GroupCreateRequest;
import com.tukorea.planding.domain.group.entity.domain.GroupRoomDomain;
import com.tukorea.planding.domain.group.service.query.GroupQueryService;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.domain.user.entity.UserDomain;
import com.tukorea.planding.global.config.s3.FileUploader;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@Builder
@RequiredArgsConstructor
public class GroupRoomFactory {

    private final FileUploader fileUploader;
    private final GroupQueryService groupQueryService;

    public GroupRoomDomain createGroupRoom(GroupCreateRequest createGroupRoom, String groupCode, UserDomain user, MultipartFile thumbnailFile) {
        GroupRoomDomain newGroupRoom = GroupRoomDomain.createGroupRoom(createGroupRoom, groupCode, user);

        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            try {
                String thumbnailUrl = fileUploader.uploadGroupThumbnail(thumbnailFile);
                newGroupRoom = newGroupRoom.updateThumbnail(thumbnailUrl);
                groupQueryService.save(newGroupRoom);
            } catch (IOException e) {
                throw new BusinessException(ErrorCode.FILE_UPLOAD_ERROR);
            }
        }

        return newGroupRoom;
    }
}

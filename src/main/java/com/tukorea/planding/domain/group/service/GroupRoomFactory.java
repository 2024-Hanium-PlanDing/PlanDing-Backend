package com.tukorea.planding.domain.group.service;

import com.tukorea.planding.domain.group.dto.request.GroupCreateRequest;
import com.tukorea.planding.domain.group.entity.GroupRoom;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.global.config.s3.S3Uploader;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class GroupRoomFactory {

    private final S3Uploader s3Uploader;

    public GroupRoom createGroupRoom(GroupCreateRequest createGroupRoom, User user, MultipartFile thumbnailFile) {
        GroupRoom newGroupRoom = GroupRoom.createGroupRoom(createGroupRoom, user);

        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            try {
                String thumbnailUrl = s3Uploader.saveGroup(thumbnailFile);
                newGroupRoom.updateThumbnail(thumbnailUrl);
            } catch (IOException e) {
                throw new BusinessException(ErrorCode.FILE_UPLOAD_ERROR);
            }
        }

        return newGroupRoom;
    }
}

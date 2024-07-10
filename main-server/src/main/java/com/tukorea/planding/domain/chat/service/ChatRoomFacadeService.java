package com.tukorea.planding.domain.chat.service;

import com.tukorea.planding.domain.chat.repository.ChatRoomRepository;
import com.tukorea.planding.domain.group.dto.request.GroupCreateRequest;
import com.tukorea.planding.domain.group.dto.response.GroupInviteAcceptResponse;
import com.tukorea.planding.domain.group.dto.response.GroupResponse;
import com.tukorea.planding.domain.group.service.GroupInviteService;
import com.tukorea.planding.domain.group.service.GroupRoomService;
import com.tukorea.planding.domain.user.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatRoomFacadeService {
    private final GroupRoomService groupRoomService;
    private final ChatRoomService chatRoomService;
    private final GroupInviteService groupInviteService;
    private final ChatRoomRepository chatRoomRepository;

    // 그룹 생성시
    public GroupResponse createGroupRoomWithChat(UserInfo userInfo, GroupCreateRequest createGroupRoom, MultipartFile thumbnailFile) {
        GroupResponse groupResponse = groupRoomService.createGroupRoom(userInfo, createGroupRoom, thumbnailFile);
        // 그룹채팅방 개설
        chatRoomService.createChatRoomForGroup(groupResponse.code());
        return groupResponse;
    }

    // 초대 수락시
    public GroupInviteAcceptResponse acceptInvitationAndChatRoom(UserInfo userInfo, String code, String groupCode) {
        GroupInviteAcceptResponse groupInviteAcceptResponse = groupInviteService.acceptInvitation(userInfo, code, groupCode);
        chatRoomRepository.enterChatRoom(groupCode);
        return groupInviteAcceptResponse;
    }

    // 그룹 떠날시
    public void leaveGroup(UserInfo userInfo, String groupCode) {
        groupRoomService.leaveGroup(userInfo, groupCode);
        chatRoomRepository.leaveChatRoom(groupCode);
    }
}

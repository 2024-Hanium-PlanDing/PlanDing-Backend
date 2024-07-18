//package com.tukorea.planding.domain.chat.controller;
//
//import com.tukorea.planding.common.CommonResponse;
//import com.tukorea.planding.domain.chat.dto.ChatRoom;
//import com.tukorea.planding.domain.chat.service.ChatRoomService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/v1/chat")
//@RequiredArgsConstructor
//public class ChatRoomController {
//    private final ChatRoomService chatRoomService;
//
//    @Transactional
//    public CommonResponse<ChatRoom> createChatRoom(){
//        chatRoomService.createChatRoomForGroup();
//    }
//}

package com.woojoobook.woojoobook.api.controller.chatroom;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.woojoobook.woojoobook.api.controller.chat.request.ChatCreateRequest;
import com.woojoobook.woojoobook.api.controller.chatroom.interceptor.StompPrincipal;
import com.woojoobook.woojoobook.api.controller.chatroom.response.ChatRoomResponse;
import com.woojoobook.woojoobook.api.service.chat.ChatService;
import com.woojoobook.woojoobook.api.service.chatroom.ChatRoomService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@Slf4j
public class ChatRoomController {

	private final ChatRoomService chatRoomService;
	private final ChatService chatService;
	private final SimpMessageSendingOperations messageOperations;

	@MessageMapping("/chat")
	public void createChatMessage(Principal principal, @Payload ChatCreateRequest request) {
		log.info("Create chat message: {}", request);
		log.info("createChatMessage: {}", principal);
		Long senderId = request.getSenderId();
		Long receiverId = request.getReceiverId();
		ChatRoomResponse findChatRoom = chatRoomService.findBySenderAndReceiver(senderId, receiverId);
		messageOperations.convertAndSendToUser(String.valueOf(request.getReceiverId()), "/sub/chat", findChatRoom);
	}
}

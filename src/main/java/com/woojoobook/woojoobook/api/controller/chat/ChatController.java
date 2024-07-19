package com.woojoobook.woojoobook.api.controller.chat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.woojoobook.woojoobook.api.controller.chat.request.ChatCreateRequest;
import com.woojoobook.woojoobook.api.controller.chat.response.ChatResponse;
import com.woojoobook.woojoobook.api.service.chat.ChatService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ChatController {

	private final ChatService chatService;
	private final SimpMessageSendingOperations messageOperations;

	@MessageMapping("/chat/{chatroomId}")
	public void send(@PathVariable("chatroomId") Long chatroomId, @RequestBody ChatCreateRequest request) {
		ChatResponse savedChat = chatService.create(request); //TODO 이벤트 발행을 먼저하도록 수정이 필요
		messageOperations.convertAndSend("/sub/chat/" + chatroomId, savedChat);
	}

	@GetMapping("/chat/{chatRoomId}")
	public ResponseEntity<Page<ChatResponse>> findPageByChatRoomId(@PathVariable("chatRoomId") Long chatRoomId,
		Pageable pageable) {
		Page<ChatResponse> responses = chatService.findAllByChatRoomId(chatRoomId, pageable);
		return ResponseEntity.ok(responses);
	}
}

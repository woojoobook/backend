package com.woojoobook.woojoobook.api.service.chat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.woojoobook.woojoobook.api.controller.chat.request.ChatCreateRequest;
import com.woojoobook.woojoobook.api.controller.chat.response.ChatResponse;
import com.woojoobook.woojoobook.api.service.chatroom.ChatRoomService;
import com.woojoobook.woojoobook.api.service.user.UserService;
import com.woojoobook.woojoobook.domain.chat.Chat;
import com.woojoobook.woojoobook.domain.chat.ChatRepository;
import com.woojoobook.woojoobook.domain.chatroom.ChatRoom;
import com.woojoobook.woojoobook.domain.user.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChatService {

	private final ChatRepository chatRepository;
	private final ChatRoomService chatRoomService;
	private final UserService userService;

	public ChatResponse create(ChatCreateRequest request) {
		ChatRoom chatRoom = chatRoomService.findDomainById(request.getChatRoomId());
		User sender = userService.findDomain(request.getSenderId());
		Chat chat = request.toEntity(chatRoom, sender);
		Chat savedChat = chatRepository.save(chat);
		return ChatResponse.of(savedChat);
	}

	public Page<ChatResponse> findAllByChatRoomId(Long chatRoomId, Pageable pageable) {
		ChatRoom chatRoom = chatRoomService.findDomainById(chatRoomId);
		return chatRepository.findAllByChatRoom(chatRoom, pageable)
			.map(ChatResponse::of);
	}
}

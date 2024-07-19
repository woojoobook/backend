package com.woojoobook.woojoobook.api.controller.chat.request;

import com.woojoobook.woojoobook.domain.chat.Chat;
import com.woojoobook.woojoobook.domain.chatroom.ChatRoom;
import com.woojoobook.woojoobook.domain.user.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatCreateRequest {

	private Long chatRoomId;
	private Long senderId;
	private Long receiverId;
	private String content;

	@Builder
	private ChatCreateRequest(Long chatRoomId, Long senderId, Long receiverId, String content) {
		this.chatRoomId = chatRoomId;
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.content = content;
	}

	public Chat toEntity(ChatRoom chatRoom, User sender) {
		return Chat.builder()
			.chatRoom(chatRoom)
			.sender(sender)
			.content(content)
			.build();
	}
}

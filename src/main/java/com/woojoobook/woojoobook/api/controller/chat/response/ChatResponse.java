package com.woojoobook.woojoobook.api.controller.chat.response;

import com.woojoobook.woojoobook.domain.chat.Chat;
import com.woojoobook.woojoobook.domain.chatroom.ChatRoom;
import com.woojoobook.woojoobook.domain.user.User;

import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatResponse {

	private Long id;
	private Long chatRoomId;
	private Long senderId;
	private String content;

	@Builder
	private ChatResponse(Long id, Long chatRoomId, Long senderId, String content) {
		this.id = id;
		this.chatRoomId = chatRoomId;
		this.senderId = senderId;
		this.content = content;
	}

	public static ChatResponse of(Chat chat) {
		return ChatResponse.builder()
			.id(chat.getId())
			.chatRoomId(chat.getChatRoom().getId())
			.senderId(chat.getSender().getId())
			.content(chat.getContent())
			.build();
	}
}

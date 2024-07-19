package com.woojoobook.woojoobook.api.controller.chatroom.request;

import com.woojoobook.woojoobook.domain.user.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomCreateRequest {

	private Long senderId;
	private Long receiverId;

	@Builder
	private ChatRoomCreateRequest(Long senderId, Long receiverId) {
		this.senderId = senderId;
		this.receiverId = receiverId;
	}
}

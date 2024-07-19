package com.woojoobook.woojoobook.api.controller.chatroom.response;

import com.woojoobook.woojoobook.domain.chatroom.ChatRoom;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatRoomResponse {

	private Long id;
	private Long senderId;
	private Long receiverId;
	private Boolean isNew; //TODO enum을 활용해서 명시적으로 표현하는 방법도 고려해보자.

	@Builder
	private ChatRoomResponse(Long id, Long senderId, Long receiverId, Boolean isNew) {
		this.id = id;
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.isNew = isNew;
	}

	public static ChatRoomResponse of(ChatRoom chatRoom, Boolean isNew) {
		return ChatRoomResponse.builder()
			.id(chatRoom.getId())
			.senderId(chatRoom.getSender().getId())
			.receiverId(chatRoom.getReceiver().getId())
			.isNew(isNew)
			.build();
	}
}

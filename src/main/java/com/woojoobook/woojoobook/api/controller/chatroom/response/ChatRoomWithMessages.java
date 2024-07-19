package com.woojoobook.woojoobook.api.controller.chatroom.response;

import org.springframework.data.domain.Page;

import com.woojoobook.woojoobook.api.controller.chat.response.ChatResponse;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomWithMessages {

	private ChatRoomResponse chatRoomResponse;
	private Page<ChatResponse> chatResponses; // 가장 최근 댓글들

	@Builder
	private ChatRoomWithMessages(ChatRoomResponse chatRoomResponse, Page<ChatResponse> chatResponses) {
		this.chatRoomResponse = chatRoomResponse;
		this.chatResponses = chatResponses;
	}
}

package com.woojoobook.woojoobook.api.service.chat;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.woojoobook.woojoobook.api.controller.chat.request.ChatCreateRequest;
import com.woojoobook.woojoobook.api.controller.chat.response.ChatResponse;
import com.woojoobook.woojoobook.domain.chat.Chat;
import com.woojoobook.woojoobook.domain.chat.ChatRepository;
import com.woojoobook.woojoobook.domain.chatroom.ChatRoom;
import com.woojoobook.woojoobook.domain.chatroom.ChatRoomRepository;
import com.woojoobook.woojoobook.domain.user.User;
import com.woojoobook.woojoobook.domain.user.UserRepository;

@SpringBootTest
class ChatServiceTest {

	@Autowired
	private ChatService chatService;

	@Autowired
	private ChatRepository chatRepository;

	@Autowired
	private ChatRoomRepository chatRoomRepository;

	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("채팅 등록에 성공한다.")
	void createSuccess() {
		// given
		User sender = createUser("sender");
		User receiver = createUser("receiver");
		userRepository.save(sender);
		userRepository.save(receiver);

		ChatRoom chatRoom = createChatRoom(sender, receiver);
		chatRoomRepository.save(chatRoom);

		ChatCreateRequest request = createChatRequest(chatRoom, sender);

		// when
		ChatResponse response = chatService.create(request);

		//then
		Chat createdChat = chatRepository.findById(response.getId()).get();
		assertThat(response).extracting("id", "chatRoomId", "senderId", "content")
			.containsExactly(createdChat.getId(), chatRoom.getId(), sender.getId(), "chat content");

	}

	private ChatCreateRequest createChatRequest(ChatRoom chatRoom, User sender) {
		return ChatCreateRequest.builder()
			.chatRoomId(chatRoom.getId())
			.senderId(sender.getId())
			.content("chat content")
			.build();
	}

	private ChatRoom createChatRoom(User sender, User receiver) {
		return ChatRoom.builder()
			.sender(sender)
			.receiver(receiver)
			.build();
	}

	private User createUser(String nickname) {
		return User.builder()
			.email("sender@test.com")
			.password("password")
			.nickname(nickname)
			.areaCode("000")
			.build();
	}
}
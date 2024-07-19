package com.woojoobook.woojoobook.api.service.chatroom;

import org.springframework.stereotype.Service;

import com.woojoobook.woojoobook.api.controller.chatroom.response.ChatRoomResponse;
import com.woojoobook.woojoobook.domain.chatroom.ChatRoom;
import com.woojoobook.woojoobook.domain.chatroom.ChatRoomRepository;
import com.woojoobook.woojoobook.domain.user.User;
import com.woojoobook.woojoobook.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChatRoomService {

	private final ChatRoomRepository chatRoomRepository;
	private final UserRepository userRepository;

	public ChatRoomResponse create(Long senderId, Long receiverId) {
		User sender = userRepository.findById(senderId).orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));
		User receiver = userRepository.findById(receiverId).orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));
		ChatRoom chatRoom = createChatRoom(sender, receiver);
		ChatRoom createdChatRoom = chatRoomRepository.save(chatRoom);
		return ChatRoomResponse.of(createdChatRoom, Boolean.TRUE);
	}

	public ChatRoom findDomainById(Long id) {
		return chatRoomRepository.findById(id).orElseThrow(() -> new RuntimeException("채팅방이 존재하지 않습니다."));
	}

	public ChatRoomResponse findBySenderAndReceiver(Long senderId, Long receiverId) {
		User sender = userRepository.findById(senderId).orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));
		User receiver = userRepository.findById(receiverId).orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));
		ChatRoom chatRoom = chatRoomRepository.findBySenderAndReceiver(sender, receiver)
			.orElseThrow(() -> new RuntimeException("채팅방이 존재하지 않습니다."));
		return ChatRoomResponse.of(chatRoom, Boolean.FALSE);
	}

	private ChatRoom createChatRoom(User sender, User receiver) {
		return ChatRoom.builder()
			.sender(sender)
			.receiver(receiver)
			.build();
	}
}

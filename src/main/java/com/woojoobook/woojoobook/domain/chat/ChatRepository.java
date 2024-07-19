package com.woojoobook.woojoobook.domain.chat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.woojoobook.woojoobook.domain.chatroom.ChatRoom;

public interface ChatRepository extends JpaRepository<Chat, Long> {

	Page<Chat> findAllByChatRoom(ChatRoom chatRoom, Pageable pageable);
}

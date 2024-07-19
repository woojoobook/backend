package com.woojoobook.woojoobook.domain.chatroom;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.woojoobook.woojoobook.domain.user.User;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

	@Query("select cr from ChatRoom cr"
		+ " where (cr.sender = :sender and cr.receiver = :receiver)"
		+ " or (cr.sender = :receiver and cr.receiver = :sender)")
	Optional<ChatRoom> findBySenderAndReceiver(@Param("sender") User sender, @Param("receiver") User receiver);
}

package com.woojoobook.woojoobook.api.controller.chatroom.interceptor;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import com.woojoobook.woojoobook.global.security.SecurityUtil;

import lombok.extern.slf4j.Slf4j;

// import com.woojoobook.woojoobook.global.security.SecurityUtil;

@Slf4j
public class StompHandshakeHandler extends DefaultHandshakeHandler {

	@Override
	protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
		Map<String, Object> attributes) {
		HttpHeaders headers = request.getHeaders();

		log.info("headers: {}", headers);
		String userId = SecurityUtil.getCurrentUsername();
		log.info("StompHandshakeHandler.userId = {}", userId);
		// String userId = UUID.randomUUID().toString();
		return new StompPrincipal("userId");
	}
}

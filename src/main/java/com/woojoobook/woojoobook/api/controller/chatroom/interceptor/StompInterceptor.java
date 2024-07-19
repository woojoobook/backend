package com.woojoobook.woojoobook.api.controller.chatroom.interceptor;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import com.woojoobook.woojoobook.global.security.SecurityUtil;
import com.woojoobook.woojoobook.global.security.jwt.JwtProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompInterceptor implements ChannelInterceptor {

	private final JwtProvider jwtProvider;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {

		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		if (accessor.getCommand() == StompCommand.CONNECT) {
			String token = accessor.getFirstNativeHeader("Authorization");
			log.info("accessor.getNativeHeader(\"Authorization\"): {}", token);
			String jwt = token.substring(7);
			if(StringUtils.hasText(jwt) && jwtProvider.validateToken(jwt)){
				Authentication authentication = jwtProvider.getAuthentication(jwt);
				SecurityContextHolder.getContext().setAuthentication(authentication);
				log.info("Security Context에 '{}' 인증 정보를 저장했습니다. uri :{}", authentication.getName());

			}

			String currentUsername = SecurityUtil.getCurrentUsername();
			log.info("currentUsername: {}", currentUsername);

			StompPrincipal simUser = (StompPrincipal)accessor.getNativeHeader("simUser");
			log.info("simUser1: {},{}", accessor, simUser);
			accessor.setNativeHeader("simUser", String.valueOf(new StompPrincipal(currentUsername)));
			log.info("simUser2: {},{}", accessor, accessor.getNativeHeader("simUser"));
			// StompPrincipal simUser = (StompPrincipal)accessor.getHeader("simUser");
			// log.info("setNativeHeader.simUser: {}", simUser.getName());
		}
		return message;
	}

	private void validateJwtToken(String accessToken) {
		if (!jwtProvider.validateToken(accessToken)) {
			throw new RuntimeException("JWT 토큰 검증에 실패했습니다.");
		}
	}
}

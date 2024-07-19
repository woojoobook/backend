package com.woojoobook.woojoobook.api.controller.chatroom.interceptor;

import java.security.Principal;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class StompPrincipal implements Principal {

	private final String name;

	public StompPrincipal(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}

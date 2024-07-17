package com.woojoobook.woojoobook.api.service.user;

import org.springframework.stereotype.Service;

import com.woojoobook.woojoobook.domain.user.User;
import com.woojoobook.woojoobook.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;

	public User findDomain(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new RuntimeException("user not found"));
	}
}

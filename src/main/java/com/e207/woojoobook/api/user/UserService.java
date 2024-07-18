package com.e207.woojoobook.api.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.e207.woojoobook.api.user.request.EmailCodeCreateRequest;
import com.e207.woojoobook.api.user.request.UserCreateRequest;
import com.e207.woojoobook.api.verification.VerificationService;
import com.e207.woojoobook.api.verification.request.VerificationMail;
import com.e207.woojoobook.domain.user.UserMasterRepository;
import com.e207.woojoobook.domain.user.UserSlaveRepository;
import com.e207.woojoobook.domain.user.UserVerification;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserSlaveRepository userSlaveRepository;
	private final UserMasterRepository userMasterRepository;
	private final PasswordEncoder passwordEncoder;
	private final VerificationService verificationService;

	@Transactional
	public void join(UserCreateRequest userCreateRequest) {
		// TODO : 예외처리
		UserVerification userVerification = this.verificationService.findByEmail(userCreateRequest.getEmail());
		if (!userVerification.isVerified()) {
			throw new RuntimeException("인증되지 않은 회원입니다.");
		}

		userCreateRequest.encode(this.passwordEncoder.encode(userCreateRequest.getPassword()));

		this.userMasterRepository.save(userCreateRequest.toEntity());
	}

	@Transactional
	public boolean verifyEmail(VerificationMail verificationMail) {
		return this.verificationService.verifyEmail(verificationMail);
	}

	@Transactional
	public void sendMail(EmailCodeCreateRequest request) {
		String email = request.getEmail();
		if (userSlaveRepository.existsByEmail(email)) {
			// TODO : 예외처리
			throw new RuntimeException("사용할 수 없는 이메일입니다.");
		}
		String verificationCode = this.verificationService.createVerificationUser(email);

		VerificationMail verificationMail = VerificationMail.builder()
			.email(email)
			.verificationCode(verificationCode)
			.build();

		this.verificationService.send(verificationMail);
	}
}

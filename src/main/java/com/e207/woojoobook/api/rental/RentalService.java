package com.e207.woojoobook.api.rental;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.e207.woojoobook.api.rental.response.RentalOfferResponse;
import com.e207.woojoobook.domain.book.Userbook;
import com.e207.woojoobook.domain.book.UserbookRepository;
import com.e207.woojoobook.domain.rental.Rental;
import com.e207.woojoobook.domain.rental.RentalRepository;
import com.e207.woojoobook.domain.user.User;
import com.e207.woojoobook.global.helper.UserHelper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RentalService {

	private final UserbookRepository userbookRepository;
	private final RentalRepository rentalRepository;
	private final UserHelper userHelper;

	@Transactional
	public RentalOfferResponse rentalOffer(Long userbooksId) {
		Userbook userbook = validateAndFindUserbook(userbooksId);
		User currentUser = this.userHelper.findCurrentUser();

		Rental rental = Rental.builder()
			.user(currentUser)
			.userbook(userbook)
			.build();
		Rental save = this.rentalRepository.save(rental);

		return new RentalOfferResponse(save.getId());
	}

	private Userbook validateAndFindUserbook(Long userbooksId) {
		Userbook userbook = this.userbookRepository.findById(userbooksId)
			.orElseThrow(() -> new RuntimeException("존재하지 않는 도서입니다."));

		if (!userbook.isAvailable()) {
			throw new RuntimeException("접근이 불가능한 도서 상태입니다.");
		}

		return userbook;
	}
}

package com.e207.woojoobook.api.rental;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.e207.woojoobook.api.rental.response.RentalOfferResponse;
import com.e207.woojoobook.domain.book.Book;
import com.e207.woojoobook.domain.book.Userbook;
import com.e207.woojoobook.domain.book.UserbookRepository;
import com.e207.woojoobook.domain.rental.Rental;
import com.e207.woojoobook.domain.rental.RentalRepository;
import com.e207.woojoobook.domain.user.User;
import com.e207.woojoobook.domain.user.UserRepository;
import com.e207.woojoobook.global.helper.UserHelper;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class RentalServiceTest {

	@Autowired
	private RentalService rentalService;
	@Autowired
	private UserbookRepository userbookRepository;
	@Autowired
	private RentalRepository rentalRepository;
	@Autowired
	private UserRepository userRepository;
	@MockBean
	private UserHelper userHelper;

	private Book book;
	private Userbook userbook;
	private User user;

	@BeforeEach
	void setUp() {
		User user = User.builder()
			.email("test@test.com")
			.nickname("nickname")
			.password("password")
			.areaCode("areaCode")
			.build();
		this.userbook = this.userbookRepository.save(new Userbook());
		this.user = this.userRepository.save(user);
	}

	@DisplayName("도서의 ID에 대해 대여를 신청한다")
	@Test
	void rentalOffer() {
		// given
		Long userbooksId = userbook.getId();
		given(this.userHelper.findCurrentUser()).willReturn(user);

		// when
		RentalOfferResponse rentalOfferResponse = this.rentalService.rentalOffer(userbooksId);

		// then
		assertNotNull(rentalOfferResponse);

		Optional<Rental> byId = this.rentalRepository.findById(rentalOfferResponse.rentalId());
		assertTrue(byId.isPresent());

		Rental createdRental = byId.get();
		assertEquals(createdRental.getUserbook().getId(), userbooksId);
		assertEquals(createdRental.getUser().getId(), user.getId());
	}

	// TODO : 예외처리
	@DisplayName("존재하지 않는 도서에 대해서는 대여 신청을 할 수 없다")
	@Test
	void rentalOffer_doseNotExist_fail() {
		// given
		Long invalidUserbookId = 241234312L;
		String expectedMessage = "존재하지 않는 도서입니다.";

		// expected
		Exception exception = assertThrows(RuntimeException.class,
			() -> this.rentalService.rentalOffer(invalidUserbookId));
		assertEquals(exception.getMessage(), expectedMessage);
	}

	// TODO : 예외처리
	@DisplayName("대여 불가능한 도서에 대해서는 대여 신청을 할 수 없다")
	@Test
	void rentalOffer_tradeStatusUnavailable_fail() {
		// given
		userbook.inactivate();
		userbook = this.userbookRepository.save(userbook);
		String expectedMessage = "접근이 불가능한 도서 상태입니다.";

		// expected
		Exception exception = assertThrows(RuntimeException.class,
			() -> this.rentalService.rentalOffer(userbook.getId()));
		assertEquals(exception.getMessage(), expectedMessage);
	}

}
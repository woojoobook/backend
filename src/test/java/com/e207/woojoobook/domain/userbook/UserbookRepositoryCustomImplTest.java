package com.e207.woojoobook.domain.userbook;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import net.bytebuddy.utility.RandomString;

import com.e207.woojoobook.domain.book.Book;
import com.e207.woojoobook.domain.user.User;

@DataJpaTest
class UserbookRepositoryCustomImplTest {

	// 쿼리에 영향을 미치는 건?
	// 사용자의 지역 코드, 제목, 저자, 등록 상태, 거래 가능 여부
	// 제목 정렬

	@Autowired
	UserbookRepository userbookRepository;
	@Autowired
	TestEntityManager em;

	@DisplayName("키워드를 입력하면 제목, 저자에 키워드가 포함된 결과를 반환한다.")
	@Test
	void findUserbookPageListByKeyword() {
		// given
		String expectedKeyword = "우주";

		UserbookFindCondition condition = new UserbookFindCondition(expectedKeyword, List.of(), null);

		List<Book> bookList = List.of(
			createBookByKeywordInTitle(expectedKeyword),
			createBookByKeywordInAuthor(expectedKeyword),
			createBookByKeywordInTitle("지구"));
		bookList.forEach(em::persist);

		String areaCode = "부산";
		List<User> userList = List.of(
			createUserByAreaCode(areaCode),
			createUserByAreaCode(areaCode),
			createUserByAreaCode(areaCode));
		userList.forEach(em::persist);

		Set<RegisterType> registerRental = Set.of(RegisterType.RENTAL);
		Set<TradeStatus> canRent = Set.of(TradeStatus.RENTAL_AVAILABLE);

		List<Userbook> userbookList = List.of(
			createUserbook(userList.get(0), bookList.get(0), registerRental, canRent),
			createUserbook(userList.get(1), bookList.get(1), registerRental, canRent),
			createUserbook(userList.get(2), bookList.get(2), registerRental, canRent)
		);
		userbookList.forEach(em::persist);

		// when
		Page<Userbook> result = userbookRepository.findUserbookPageList(condition, PageRequest.of(0, 10));

		// then
		List<Userbook> content = result.getContent();
		assertThat(content).isNotEmpty();
		assertThat(content).map(Userbook::getBook)
			.allMatch(book -> book.getTitle().contains(expectedKeyword) || book.getAuthor().contains(expectedKeyword));
	}

	@DisplayName("지역 코드 목록을 입력해서 사용자 도서를 필터링한다.")
	void findUserbookPageListByAreaCode() {
		// given
		UserbookFindCondition condition = new UserbookFindCondition(null, List.of("대구", "대전"), null);

		List<Book> bookList = List.of(
			createBook(),
			createBook(),
			createBook(),
			createBook(),
			createBook());
		bookList.forEach(em::persist);

		List<User> userList = List.of(
			createUserByAreaCode("부산"),
			createUserByAreaCode("부산"),
			createUserByAreaCode("대구"),
			createUserByAreaCode("대전"),
			createUserByAreaCode("대전"));
		userList.forEach(em::persist);

		Set<RegisterType> registerRental = Set.of(RegisterType.RENTAL);
		Set<TradeStatus> canRent = Set.of(TradeStatus.RENTAL_AVAILABLE);

		List<Userbook> userbookList = List.of(
			createUserbook(userList.get(0), bookList.get(0), registerRental, canRent),
			createUserbook(userList.get(1), bookList.get(1), registerRental, canRent),
			createUserbook(userList.get(2), bookList.get(2), registerRental, canRent),
			createUserbook(userList.get(3), bookList.get(3), registerRental, canRent),
			createUserbook(userList.get(4), bookList.get(4), registerRental, canRent)
		);
		userbookList.forEach(em::persist);

		// when
		userbookRepository.findUserbookPageList(condition, PageRequest.of(0, 10));

		// then
	}

	private User createUserByAreaCode(String areaCode) {
		return User.builder()
			.email(RandomString.make())
			.password(RandomString.make())
			.nickname(RandomString.make())
			.areaCode(areaCode)
			.build();
	}

	private Book createBook() {
		return Book.builder()
			.isbn(UUID.randomUUID().toString())
			.title(RandomString.make())
			.author(RandomString.make())
			.build();
	}

	private Book createBookByKeywordInTitle(String keyword) {
		String prefix = RandomString.make(5);

		String suffix = RandomString.make(5);
		String author = RandomString.make(5);
		String title = prefix + keyword + suffix;

		return Book.builder()
			.isbn(UUID.randomUUID().toString())
			.title(title)
			.author(author)
			.build();
	}

	private Book createBookByKeywordInAuthor(String keyword) {
		String title = RandomString.make(5);

		String prefix = RandomString.make(5);
		String suffix = RandomString.make(5);
		String author = prefix + keyword + suffix;

		return Book.builder()
			.isbn(UUID.randomUUID().toString())
			.title(title)
			.author(author)
			.build();
	}

	private Userbook createUserbook(User user, Book book, Set<RegisterType> registerTypes, Set<TradeStatus> tradeStatuses) {
		return Userbook.builder()
			.user(user)
			.book(book)
			.registerType(registerTypes)
			.tradeStatus(tradeStatuses)
			.qualityStatus(QualityStatus.GOOD)
			.build();
	}
}
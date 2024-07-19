package com.e207.woojoobook.api.book;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.e207.woojoobook.api.book.request.BookFindRequest;
import com.e207.woojoobook.api.book.response.BookListResponse;
import com.e207.woojoobook.api.book.response.BookResponse;
import com.e207.woojoobook.api.book.response.NaverBookApiResponse;
import com.e207.woojoobook.api.book.response.NaverBookItem;

@Service
public class BookService {

	@Value("${naver-client-key}")
	private String naver_client_id;

	@Value("${naver-client-secret}")
	private String naver_client_secret;

	public BookListResponse findBookList(BookFindRequest request) {
		String keyword = request.keyword().trim();
		int page = request.page();
		int size = 20;

		if (keyword.isEmpty()) {
			return new BookListResponse(new ArrayList<>());
		}

		RestClient restClient = RestClient.builder()
			.baseUrl("https://openapi.naver.com")
			.build();

		NaverBookApiResponse NaverResponse = restClient.get()
			.uri(uriBuilder -> uriBuilder.path("/v1/search/book.json")
				.queryParam("query", keyword)
				.queryParam("start", page)
				.queryParam("display", size)
				.build())
			.headers(httpHeaders -> {
				httpHeaders.set("X-Naver-Client-Id", naver_client_id);
				httpHeaders.set("X-Naver-Client-Secret", naver_client_secret);
			})
			.retrieve()
			.body(NaverBookApiResponse.class);

		List<BookResponse> bookResponses = new ArrayList<>();
		if (NaverResponse != null && NaverResponse.getItems() != null) {
			for (NaverBookItem item : NaverResponse.getItems()) {
				BookResponse bookResponse = BookResponse.builder()
					.isbn(item.getIsbn())
					.title(item.getTitle())
					.author(item.getAuthor())
					.publisher(item.getPublisher())
					.publicationDate(LocalDate.parse(item.getPubdate(), DateTimeFormatter.ofPattern("yyyyMMdd")))
					.thumbnail(item.getImage())
					.description(item.getDescription())
					.build();

				bookResponses.add(bookResponse);
			}
		}

		return new BookListResponse(bookResponses);
	}
}
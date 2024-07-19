package com.e207.woojoobook.api.book.response;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookResponse {
	private String isbn;
	private String title;
	private String author;
	private String publisher;
	private LocalDate publicationDate;
	private String thumbnail;
	private String description;
}

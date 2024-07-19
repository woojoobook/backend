package com.e207.woojoobook.api.book.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NaverBookItem {
	private String title;
	private String link;
	private String image;
	private String author;
	private String price;
	private String discount;
	private String publisher;
	private String pubdate;
	private String isbn;
	private String description;
}
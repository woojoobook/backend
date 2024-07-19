package com.e207.woojoobook.api.book.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NaverBookApiResponse {
	private List<NaverBookItem> items;
}
package com.e207.woojoobook.api.userbook;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.e207.woojoobook.api.userbook.request.UserbookPageFindRequest;
import com.e207.woojoobook.api.userbook.response.UserbookResponse;
import com.e207.woojoobook.domain.userbook.UserbookService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserbookController {

	private final UserbookService userbookService;

	@GetMapping("/userbooks")
	public ResponseEntity<?> findUserbookList(
		@Valid
		UserbookPageFindRequest condition,
		@PageableDefault(sort="createdAt")
		Pageable pageable,
		Errors errors
	) {
		if (errors.hasErrors()) {
			return new ResponseEntity<>(errors.getAllErrors(), HttpStatus.BAD_REQUEST);
		}

		Page<UserbookResponse> userbookPageResult = userbookService.businessUserbookSearch(condition, pageable);

		return ResponseEntity.ok(userbookPageResult);
	}
}

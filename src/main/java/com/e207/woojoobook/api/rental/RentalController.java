package com.e207.woojoobook.api.rental;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class RentalController {

	private final RentalService rentalService;

	@PostMapping("/userbooks/{userbooksId}/rentals/offer")
	public ResponseEntity<?> createRentalOffer(@PathVariable("userbooksId") Long userbooksId) {
		return ResponseEntity.status(HttpStatus.CREATED).body(this.rentalService.rentalOffer(userbooksId));
	}
}
package com.e207.woojoobook.api.rental;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.e207.woojoobook.api.rental.response.RentalOfferResponse;
import com.e207.woojoobook.domain.user.UserRepository;
import com.e207.woojoobook.global.security.SecurityConfig;

@Import({SecurityConfig.class})
@WebMvcTest(controllers = RentalController.class)
class RentalControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private RentalService rentalService;
	@MockBean
	private UserRepository userRepository;

	@WithMockUser
	@DisplayName("회원이 도서에 대한 대여를 신청한다")
	@Test
	void createRentalRequest() throws Exception {
		// given
		Long targetBookId = 1L;
		Long validRentalId = 2L;
		given(rentalService.rentalOffer(targetBookId)).willReturn(new RentalOfferResponse(validRentalId));

		// when
		ResultActions resultActions = this.mockMvc.perform(
			post("/userbooks/{targetBookId}/rentals/offer", targetBookId));

		// then
		resultActions.andExpect(status().isCreated())
			.andExpect(jsonPath("$.rentalId").exists());
	}
}
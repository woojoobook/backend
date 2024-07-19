package com.e207.woojoobook.domain.userbook;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.e207.woojoobook.api.userbook.request.UserbookPageFindRequest;
import com.e207.woojoobook.api.userbook.response.UserbookResponse;

@Service
public class UserbookService {

	private final Integer MAX_AREA_CODE_SIZE;
	private final UserbookRepository userbookSlaveRepository;

	public UserbookService(@Value("${userbook.search.ereacode.count}") Integer MAX_AREA_CODE_SIZE,
							UserbookRepository userbookSlaveRepository) {
		this.MAX_AREA_CODE_SIZE = MAX_AREA_CODE_SIZE;
		this.userbookSlaveRepository = userbookSlaveRepository;
	}

	public Page<UserbookResponse> businessUserbookSearch(UserbookPageFindRequest request, Pageable pageable) {
		// TODO: 예외 처리
		if (request.areaCodeList().size() > MAX_AREA_CODE_SIZE) {
			throw new RuntimeException("지역 선택이 초과 했을 때 던지는 에러");
		}

		var userbookPageList = userbookSlaveRepository.findUserbookPageList(UserbookFindCondition.of(request), pageable);

		return userbookPageList.map(UserbookResponse::of);
	}
}

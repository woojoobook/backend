package com.e207.woojoobook.domain.userbook;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserbookRepositoryCustom {
	Page<Userbook> findUserbookPageList(UserbookFindCondition condition, Pageable pageable);
}

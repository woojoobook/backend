package com.e207.woojoobook.domain.userbook;

import static com.e207.woojoobook.domain.book.QBook.*;
import static com.e207.woojoobook.domain.userbook.QUserbook.*;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

public class UserbookRepositoryCustomImpl implements UserbookRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public UserbookRepositoryCustomImpl(EntityManager em) {
		queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public Page<Userbook> findUserbookPageList(UserbookFindCondition condition, Pageable pageable) {

		List<Userbook> contents = queryFactory
			.selectFrom(userbook)
			.join(userbook.book, book)
			.where(
				isContainTitleOrAuthor(condition.keyword()),
				isInAreaCodeList(condition.areaCodeList()),
				isContainRegisterType(condition.registerType()))
			.orderBy(getOrderSpecifierList(pageable.getSort()))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long count = queryFactory
			.select(userbook.count())
			.from(userbook)
			.join(userbook.book, book)
			.where(
				isContainTitleOrAuthor(condition.keyword()),
				isInAreaCodeList(condition.areaCodeList()),
				canTrade(condition.registerType()),
				isContainRegisterType(condition.registerType()))
			.fetchOne();

		return new PageImpl<>(contents, pageable, count);
	}

	private BooleanExpression isContainTitleOrAuthor(String keyword) {
		return StringUtils.hasText(keyword) ?
			book.title.contains(keyword.trim()).or(book.author.contains(keyword.trim())) : null;
	}

	private BooleanExpression isInAreaCodeList(List<String> codeList) {
		return codeList.isEmpty() ?
			null : userbook.areaCode.in(codeList);
	}
	
	private BooleanExpression canTrade(RegisterType registerType) {
		if (registerType == null) {
			return userbook.tradeStatus.contains(TradeStatus.RENTAL_AVAILABLE)
				.and(userbook.tradeStatus.contains(TradeStatus.EXCHANGE_AVAILABLE));
		}

		return switch (registerType) {
			case RENTAL -> userbook.tradeStatus.contains(TradeStatus.RENTAL_AVAILABLE);
			case EXCHANGE -> userbook.tradeStatus.contains(TradeStatus.EXCHANGE_AVAILABLE);
		};
	}

	private BooleanExpression isContainRegisterType(RegisterType registerType) {
		return Objects.isNull(registerType) ? null : userbook.registerType.contains(registerType);
	}
	

	private OrderSpecifier[] getOrderSpecifierList(Sort sort) {
		PathBuilder pathBuilder = new PathBuilder(Userbook.class, "userbook");

		return sort.stream().map(order -> {
			Order direction = order.isAscending() ? Order.ASC : Order.DESC;
			return new OrderSpecifier(direction, pathBuilder.get(order.getProperty()));
		}).toArray(OrderSpecifier[]::new);
	}
}

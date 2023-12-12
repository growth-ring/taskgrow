package com.growth.task.review.repository.impl;

import com.growth.task.review.dto.ReviewDetailResponse;
import com.growth.task.review.dto.ReviewDetailWithTaskDateResponse;
import com.growth.task.review.dto.ReviewStatsRequest;
import com.growth.task.review.repository.ReviewRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.growth.task.review.domain.QReview.review;
import static com.growth.task.task.domain.QTasks.tasks;

@Repository
public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public ReviewRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<ReviewDetailResponse> findByUserIdAndBetweenTimeRange(Long userId, ReviewStatsRequest request) {
        return queryFactory.select(Projections.fields(ReviewDetailResponse.class,
                        review.id.as("reviewId"),
                        review.subject,
                        review.contents,
                        review.feelingsScore
                ))
                .from(review)
                .innerJoin(tasks)
                .on(review.tasks.eq(tasks))
                .where(
                        eqUserId(userId),
                        betweenTimeRange(request.getStartDate(), request.getEndDate())
                )
                .fetch()
                ;
    }

    @Override
    public Page<ReviewDetailWithTaskDateResponse> findByUserAndParams(
            Pageable pageable,
            Long userId,
            Integer feelingsScore,
            LocalDate startDate,
            LocalDate endDate
    ) {
        List<ReviewDetailWithTaskDateResponse> content = queryFactory
                .select(Projections.fields(ReviewDetailWithTaskDateResponse.class,
                        review.id.as("reviewId"),
                        review.subject,
                        review.feelingsScore,
                        tasks.taskDate
                ))
                .from(review)
                .innerJoin(tasks)
                .on(review.tasks.eq(tasks))
                .where(
                        eqUserId(userId),
                        eqFeelingsScore(feelingsScore),
                        betweenTimeRange(startDate, endDate)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(tasks.taskDate.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(review.count())
                .from(review)
                .innerJoin(tasks)
                .on(review.tasks.eq(tasks))
                .where(
                        eqUserId(userId),
                        eqFeelingsScore(feelingsScore),
                        betweenTimeRange(startDate, endDate)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression eqUserId(Long userId) {
        return tasks.user.userId.eq(userId);
    }

    private BooleanExpression betweenTimeRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null && endDate == null) {
            return null;
        }
        return tasks.taskDate.between(startDate, endDate);
    }

    private BooleanExpression eqFeelingsScore(Integer feelingsScore) {
        System.out.println("repository feelingsScore: " + feelingsScore);
        if (feelingsScore == null) {
            return null;
        }
        return review.feelingsScore.eq(feelingsScore);
    }
}

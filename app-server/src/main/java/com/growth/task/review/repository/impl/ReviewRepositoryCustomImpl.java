package com.growth.task.review.repository.impl;

import com.growth.task.review.dto.ReviewDetailResponse;
import com.growth.task.review.dto.ReviewStatsRequest;
import com.growth.task.review.repository.ReviewRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
                        review.contents,
                        review.feelingsScore
                ))
                .from(review)
                .leftJoin(tasks)
                .on(review.tasks.eq(tasks))
                .where(
                        eqUserId(userId),
                        betweenTimeRange(request)
                )
                .fetch()
                ;
    }

    private static BooleanExpression eqUserId(Long userId) {
        return tasks.user.userId.eq(userId);
    }

    private static BooleanExpression betweenTimeRange(ReviewStatsRequest request) {
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();
        if (startDate == null && endDate == null) {
            return null;
        }
        return tasks.taskDate.between(startDate, endDate);
    }
}

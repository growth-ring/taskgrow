package com.growth.task.review.exception;

public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(Long reviewId) {
        super("리뷰를 찾을 수 없습니다. review id =" + reviewId);
    }
}

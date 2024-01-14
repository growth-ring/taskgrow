package com.growth.task.review.exception;

import com.growth.task.global.error.exception.EntityNotFoundException;

public class ReviewNotFoundException extends EntityNotFoundException {
    public ReviewNotFoundException(Long reviewId) {
        super("리뷰를 찾을 수 없습니다. review id =" + reviewId);
    }
    public ReviewNotFoundException(String message){
        super(message);
    }
}

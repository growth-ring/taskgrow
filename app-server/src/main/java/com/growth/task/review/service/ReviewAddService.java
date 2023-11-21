package com.growth.task.review.service;

import com.growth.task.review.domain.Review;
import com.growth.task.review.dto.ReviewAddRequest;
import com.growth.task.review.dto.ReviewAddResponse;
import com.growth.task.review.exception.AlreadyReviewException;
import com.growth.task.review.repository.ReviewRepository;
import com.growth.task.task.domain.Tasks;
import com.growth.task.task.service.TaskDetailService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ReviewAddService {
    private final ReviewRepository reviewRepository;
    private final TaskDetailService taskDetailService;

    public ReviewAddService(ReviewRepository reviewRepository, TaskDetailService taskDetailService) {
        this.reviewRepository = reviewRepository;
        this.taskDetailService = taskDetailService;
    }

    @Transactional
    public ReviewAddResponse save(ReviewAddRequest request) {
        Tasks task = taskDetailService.findTaskById(request.getTaskId());
        try {
            Review review = reviewRepository.save(request.toEntity(task));
            return new ReviewAddResponse(review.getId());
        } catch (DataIntegrityViolationException exception) {
            throw new AlreadyReviewException("이미 작성된 회고가 있습니다.");
        }
    }
}

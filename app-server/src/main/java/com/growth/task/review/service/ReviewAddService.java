package com.growth.task.review.service;

import com.growth.task.review.domain.Review;
import com.growth.task.review.dto.ReviewAddRequest;
import com.growth.task.review.dto.ReviewAddResponse;
import com.growth.task.review.exception.AlreadyReviewException;
import com.growth.task.review.exception.InvalidReviewRequestException;
import com.growth.task.review.repository.ReviewRepository;
import com.growth.task.task.domain.Tasks;
import com.growth.task.task.service.TaskDetailService;
import com.growth.task.todo.repository.TodosRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ReviewAddService {
    private final ReviewRepository reviewRepository;
    private final TaskDetailService taskDetailService;
    private final TodosRepository todosRepository;

    public ReviewAddService(
            ReviewRepository reviewRepository,
            TaskDetailService taskDetailService,
            TodosRepository todosRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.taskDetailService = taskDetailService;
        this.todosRepository = todosRepository;
    }

    @Transactional
    public ReviewAddResponse save(ReviewAddRequest request) {
        Tasks task = taskDetailService.findTaskById(request.getTaskId());

        validate(task);

        try {
            Review review = reviewRepository.save(request.toEntity(task));
            return new ReviewAddResponse(review.getId());
        } catch (DataIntegrityViolationException exception) {
            throw new AlreadyReviewException("이미 작성된 회고가 있습니다.");
        }
    }

    private void validate(Tasks task) {
        if (!hasTodo(task)) {
            throw new InvalidReviewRequestException(task.getTaskDate());
        }
    }

    @Transactional(readOnly = true)
    private boolean hasTodo(Tasks task) {
        return todosRepository.existsByTask_TaskId(task.getTaskId());
    }
}

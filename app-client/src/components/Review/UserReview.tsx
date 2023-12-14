import { useState, useEffect } from 'react';
import styled from 'styled-components';
import ReviewSubject from './ReviewSubject';
import ReviewContent from './ReviewContent';
import { BsTrash3, BsCheckCircleFill } from 'react-icons/bs';
import { useTask } from '../../store/task';
import { useTimerStore } from '../../store/timer';
import { useReviewStore } from '../../store/review';
import {
  addReview,
  getReview,
  deleteReview,
  updateReview,
} from '../../services/review';

const Button = styled.button`
  &:hover {
    color: var(--main-color);
  }
`;

const ButtonBox = styled.div`
  display: flex;
  justify-content: right;
  margin: 0 auto;

  @media (max-width: 1023px) {
    width: 280px;
    font-size: 20px;
    margin-bottom: 2rem;
  }

  @media (min-width: 1024px) {
    width: 450px;
    font-size: 25px;
  }
`;

const UserReview = () => {
  const [inputContent, setInputContent] = useState('');
  const [inputSubject, setInputSubject] = useState('');
  const timer = useTimerStore();
  const { selectedTaskId } = useTask();
  const {
    feelingsScore,
    setFeelingsScore,
    setReviewId,
    reviewId,
    closeReview,
  } = useReviewStore();

  const getInputContent = (content: string) => {
    setInputContent(content);
  };

  const getInputSubject = (subject: string) => {
    setInputSubject(subject);
  };

  const handleAddReview = async () => {
    if (reviewId !== 0) {
      const reviewData = {
        reviewId: reviewId,
        subject: inputSubject,
        contents: inputContent,
        feelingsScore: feelingsScore,
      };
      await updateReview(reviewData);
    } else {
      const reviewData = {
        taskId: selectedTaskId,
        subject: inputSubject,
        contents: inputContent,
        feelingsScore: feelingsScore,
      };
      const isAddReview = await addReview(reviewData);
      if (isAddReview) {
        setReviewId(isAddReview.review_id);
        alert('회고 추가 되었습니다.');
      }
    }
  };

  const handleDeleteReview = async () => {
    const isDelete = await deleteReview(reviewId);
    if (isDelete) {
      alert('회고 삭제 되었습니다.');
      closeReview();
      timer.showTodo();
    }
  };

  const findReview = async () => {
    const review = await getReview(selectedTaskId);
    if (review) {
      setInputContent(review.contents);
      setInputSubject(review.subject);
      setFeelingsScore(review.feelings_score);
      setReviewId(review.review_id);
    }
  };

  useEffect(() => {
    findReview();
  }, []);

  return (
    <>
      <ReviewSubject subject={inputSubject} getInputSubject={getInputSubject} />
      <ReviewContent content={inputContent} getInputContent={getInputContent} />
      <ButtonBox>
        {reviewId !== 0 && (
          <Button onClick={handleDeleteReview} style={{ margin: '0 1rem' }}>
            <BsTrash3 />
          </Button>
        )}
        <Button onClick={handleAddReview}>
          <BsCheckCircleFill />
        </Button>
      </ButtonBox>
    </>
  );
};
export default UserReview;

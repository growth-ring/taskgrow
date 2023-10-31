import { useState, useEffect } from 'react';
import styled from 'styled-components';
import { BsTrash3, BsCheckCircleFill } from 'react-icons/bs';
import { useTask } from '../../store/task';
import { useReviewStore } from '../../store/review';
import { addReview, getReview, deleteReview } from '../../services/review';

const Button = styled.button`
  &:hover {
    color: var(--main-color);
  }
`;

const Review = styled.textarea`
  margin: 0 auto;
  background-color: white;
  padding: 1rem;
  border-radius: 1rem;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
  resize: none;
  margin-bottom: 1rem;

  @media (max-width: 1023px) {
    width: 300px;
    height: 210px;
  }

  @media (min-width: 1024px) {
    width: 470px;
    height: 270px;
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
  const { selectedTaskId } = useTask();
  const { feelingsScore, setFeelingsScore, setReviewId, reviewId } =
    useReviewStore();
  const [inputValue, setInputValue] = useState<string>('');

  const handleInputChange = (event: React.ChangeEvent<HTMLTextAreaElement>) => {
    setInputValue(event.target.value);
  };

  const handleAddReview = async () => {
    const reviewData = {
      taskId: selectedTaskId,
      content: inputValue,
      feelingsScore: feelingsScore,
    };
    const isAddReview = await addReview(reviewData);
    if (isAddReview) {
      setReviewId(isAddReview.review_id);
      alert('회고 추가 되었습니다.');
    }
  };

  const handleDeleteReview = async () => {
    const isDelete = await deleteReview(reviewId);
    if (isDelete) {
      alert('회고 삭제 되었습니다.');
      setReviewId(0);
      setInputValue('');
    }
  };

  const findReview = async () => {
    const review = await getReview(selectedTaskId);
    if (review) {
      setInputValue(review.contents);
      setFeelingsScore(review.feelings_score);
      setReviewId(review.review_id);
    }
  };

  useEffect(() => {
    findReview();
  }, []);

  return (
    <>
      <Review value={inputValue} onChange={handleInputChange}></Review>
      <ButtonBox>
        <Button onClick={handleDeleteReview} style={{ margin: '0 1rem' }}>
          <BsTrash3 />
        </Button>
        <Button onClick={handleAddReview}>
          <BsCheckCircleFill />
        </Button>
      </ButtonBox>
    </>
  );
};
export default UserReview;

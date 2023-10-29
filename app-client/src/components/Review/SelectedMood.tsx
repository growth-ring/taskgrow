import styled from 'styled-components';
import { useReviewStore } from '../../store/review';

const Score = styled.div`
  background-color: var(--sub-yellow-color);
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  color: var(--main-color);
  margin-bottom: 2rem;
  width: 70px;
  height: 70px;
  font-size: 25px;
`;

const SelectedMood = () => {
  const { feelingsScore } = useReviewStore();

  return <Score> {feelingsScore === 0 ? '?' : feelingsScore} </Score>;
};

export default SelectedMood;

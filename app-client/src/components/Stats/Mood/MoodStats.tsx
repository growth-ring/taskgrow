import styled from 'styled-components';
import Box from '../UI/Box';
import { useMoods } from '../../../store/mood';

const Container = styled.div`
  display: flex;
  justify-content: center;
  text-align: center;

  @media (min-width: 768px) {
    width: 50%;
    flex-direction: column;
  }
`;

const MoodStats = () => {
  const { moods, topMoodsComments } = useMoods();

  const total = moods.reduce((acc, { num }) => acc + num, 0).toString();
  const title = ['감정', total];

  return (
    <Container>
      <Box
        title={title}
        comment={topMoodsComments.comment}
        subComment={topMoodsComments.subComment}
      />
    </Container>
  );
};

export default MoodStats;

import styled from 'styled-components';
import Box from '../UI/Box';
import { useMoods } from '../../../store/mood';

const Container = styled.div`
  width: 50%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  text-align: center;

  @media (max-width: 1023px) {
    width: 90%;
    margin: 50px auto 0 auto;
    padding-bottom: 50px;
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

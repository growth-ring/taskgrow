import styled from 'styled-components';
import Box from '../UI/Box';

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
  const Title = ['감정', '20'];
  const firstMood = '기분 좋은 날';
  const secondMood = '행복한 날';
  const comment = `${firstMood}이 가장 많았어요`;
  const subComment = `두번째로는 ${secondMood}이 많았어요`;

  return (
    <Container>
      <Box title={Title} comment={comment} subComment={subComment} />
    </Container>
  );
};

export default MoodStats;

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
  const maxMood = '기분 좋은 날';
  const comment = `${maxMood}이 제일 많았어요`;
  const subComment = '달력에서 자세히 볼 수 있어요 🗓️';

  return (
    <Container>
      <Box title={Title} comment={comment} subComment={subComment} />
    </Container>
  );
};

export default MoodStats;

import styled from 'styled-components';
import Box from '../UI/Box';
import { TodosComment } from '../../../constants/StatsComment';

const Container = styled.div`
  display: flex;
  justify-content: center;
  text-align: center;

  @media (min-width: 768px) {
    width: 50%;
    flex-direction: column;
  }
`;

const TodosStats = () => {
  const Title = ['한 일', '30'];
  const percent = 80;
  const comment = `완료 달성률은 ${percent}% 이에요`;
  const subComment =
    TodosComment.find((comments) => comments.percent === percent)?.comment ||
    '';

  return (
    <Container>
      <Box title={Title} comment={comment} subComment={subComment} />
    </Container>
  );
};

export default TodosStats;

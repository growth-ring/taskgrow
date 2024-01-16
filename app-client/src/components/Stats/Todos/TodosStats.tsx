import styled from 'styled-components';
import Box from '../UI/Box';
import { TodosComment } from '../../../constants/StatsComment';
import { useTodosStore } from '../../../store/todos';

const Container = styled.div`
  width: 50%;
  margin-right: 15px;

  @media (max-width: 1023px) {
    width: 90%;
    margin: 50px auto 0 auto;
  }
`;

const TodosStats = () => {
  const { todosStats } = useTodosStore();
  const { totalCount, doneCount } = todosStats;

  const Title = ['한 일', totalCount.toString()];
  const percent =
    totalCount === 0 ? 0 : Math.floor((doneCount / totalCount) * 100);
  const comment =
    totalCount === 0
      ? '기록된 한 일이 없어요'
      : `완료 달성률은 ${percent}% 이에요`;
  const subComment =
    totalCount === 0
      ? '할 일을 정해볼까요?'
      : TodosComment.find((comments) => comments.percent >= percent)?.comment ||
        '';

  return (
    <Container>
      <Box title={Title} comment={comment} subComment={subComment} />
    </Container>
  );
};

export default TodosStats;

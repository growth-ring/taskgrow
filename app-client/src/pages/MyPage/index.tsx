import { useEffect } from 'react';
import styled from 'styled-components';
import Header from '../../components/Menu/Header';
import HeaderDate from '../../components/Stats/HeaderDate';
import TodosStats from '../../components/Stats/Todos/TodosStats';
import MoodStats from '../../components/Stats/Mood/MoodStats';
import { useMoods } from '../../store/mood';
import { useTodosStore } from '../../store/todos';
import { useDate } from '../../store/stats';

const Line = styled.div`
  background-color: #f5f5f5;
`;

const Container = styled.div`
  max-width: 1024px;
  margin: 50px auto 0 auto;
`;

const Wrap = styled.div`
  @media (min-width: 1024px) {
    display: flex;
  }
`;

const MyPage = () => {
  const { year, month } = useDate();
  const { getMoods, findTopMoods, moods } = useMoods();
  const { getTodos } = useTodosStore();

  useEffect(() => {
    getMoods();
    getTodos();
  }, [year, month]);

  useEffect(() => {
    findTopMoods();
  }, [moods]);

  return (
    <>
      <Header title="통계" />
      <Line style={{ height: '2px' }} />
      <HeaderDate />
      <Container>
        <Wrap>
          <TodosStats />
          <MoodStats />
        </Wrap>
      </Container>
    </>
  );
};

export default MyPage;

import styled from 'styled-components';
import { CircleType } from '../UI/Detail';
import {
  FaRegCalendarCheck,
  FaRegCalendarMinus,
  FaRegCalendarXmark,
} from 'react-icons/fa6';
import { useTodosStore } from '../../../store/todos';

const Container = styled.div`
  display: flex;
  justify-content: center;
  color: white;
`;

const Stats = styled.button<{ bg: string }>`
  aspect-ratio: 1 / 1;

  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background-color: ${(props) => props.bg};
  width: 10rem;

  @media (max-width: 767px) {
    font-size: 14px;
    margin: 5px 2px;
  }

  @media (min-width: 768px) and (max-width: 1023px) {
    font-size: 16px;
    margin: 10px 5px;
  }

  @media (min-width: 1024px) {
    font-size: 18px;
    margin: 20px 10px;
  }
`;

const StatsIcon = styled.div`
  color: ${(props) => props.color};

  @media (max-width: 767px) {
    width: 20px;
    height: 20px;
    margin-bottom: 7px;
  }

  @media (min-width: 768px) and (max-width: 1023px) {
    width: 25px;
    height: 25px;
    margin-bottom: 10px;
  }

  @media (min-width: 1024px) {
    width: 30px;
    height: 30px;
    margin-bottom: 15px;
  }
`;

const Title = styled.div`
  margin-bottom: 6px;

  @media (max-width: 767px) {
    margin-bottom: 3px;
  }
`;

const Count = styled.div`
  font-weight: bold;
`;

const Circle = ({ getIsDetail }: CircleType) => {
  const { todosStats } = useTodosStore();

  const handleOnDetail = () => {
    getIsDetail(true);
  };

  return (
    <Container>
      <Stats bg="var(--main-color)" onClick={handleOnDetail}>
        <StatsIcon as={FaRegCalendarCheck} />
        <Title>완료</Title>
        <Count>{todosStats.done_count}</Count>
      </Stats>
      <Stats bg="var(--sub-blue-color)" onClick={handleOnDetail}>
        <StatsIcon as={FaRegCalendarMinus} />
        <Title>진행중</Title>
        <Count>{todosStats.progress_count}</Count>
      </Stats>
      <Stats bg="var(--line-color)" onClick={handleOnDetail}>
        <StatsIcon as={FaRegCalendarXmark} />
        <Title>미완료</Title>
        <Count>{todosStats.undone_count}</Count>
      </Stats>
    </Container>
  );
};

export default Circle;

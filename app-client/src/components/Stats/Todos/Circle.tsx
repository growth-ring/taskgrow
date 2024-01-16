import styled from 'styled-components';
import { CircleType } from '../UI/Detail';
import {
  FaRegCalendarCheck,
  FaRegCalendarMinus,
  FaRegCalendarXmark,
} from 'react-icons/fa6';
import { useTodosStore } from '../../../store/todos';
import { useStats } from '../../../store/stats';

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
  const { todosStats, getTodoDetail } = useTodosStore();
  const { setTodosDetail, setTodosTotal } = useStats();

  const handleOnDetail = ({
    category,
    categoryText,
    total,
  }: {
    category: string;
    categoryText: string;
    total: number;
  }) => {
    setTodosDetail(category);
    setTodosTotal(total);
    getIsDetail({ action: true, category: categoryText });
    getTodoDetail({ status: category, page: 1 });
  };

  return (
    <Container>
      <Stats
        bg="var(--main-color)"
        onClick={() =>
          handleOnDetail({
            category: 'DONE',
            categoryText: '한 일(완료) 상세보기',
            total: todosStats.doneCount,
          })
        }
      >
        <StatsIcon as={FaRegCalendarCheck} />
        <Title>완료</Title>
        <Count>{todosStats.doneCount}</Count>
      </Stats>
      <Stats
        bg="var(--sub-blue-color)"
        onClick={() =>
          handleOnDetail({
            category: 'PROGRESS',
            categoryText: '한 일(진행중) 상세보기',
            total: todosStats.progressCount,
          })
        }
      >
        <StatsIcon as={FaRegCalendarMinus} />
        <Title>진행중</Title>
        <Count>{todosStats.progressCount}</Count>
      </Stats>
      <Stats
        bg="var(--line-color)"
        onClick={() =>
          handleOnDetail({
            category: 'READY',
            categoryText: '한 일(미완료) 상세보기',
            total: todosStats.undoneCount - todosStats.progressCount,
          })
        }
      >
        <StatsIcon as={FaRegCalendarXmark} />
        <Title>미완료</Title>
        <Count>{todosStats.undoneCount - todosStats.progressCount}</Count>
      </Stats>
    </Container>
  );
};

export default Circle;

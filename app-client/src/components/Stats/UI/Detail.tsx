import styled from 'styled-components';
import { useMoods } from '../../../store/mood';
import { useTodosStore } from '../../../store/todos';

import { useNavigate } from 'react-router-dom';

export interface CircleType {
  getIsDetail: (params: {
    action: boolean;
    category?: string;
    subject?: string;
  }) => void;
}

const Container = styled.div`
  width: 30rem;

  @media (max-width: 767px) {
    font-size: 13px;
  }

  @media (min-width: 768px) {
    font-size: 16px;
  }
`;

const TitleBox = styled.div`
  display: flex;
  height: 20px;
  justify-content: space-between;
  align-items: center;
  margin: 6px 0;
  color: #949494;
`;

const Line = styled.div`
  background-color: #f5f5f5;
`;

const Content = styled.div`
  height: 220px;
`;

const Text = styled.button`
  width: 100%;
  display: flex;
  justify-content: space-between;
  margin-top: 6.5px;
`;

const TextTitle = styled.div`
  text-align: left;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;

  @media (max-width: 767px) {
    width: 200px;
  }

  @media (min-width: 768px) {
    width: 350px;
  }
`;

const Detail = ({ category }: { category: string }) => {
  const navigate = useNavigate();
  const { moodDetail } = useMoods();
  const { todoDetail } = useTodosStore();
  const isMoon = category.includes('감정');

  const handleTodoPage = (userClickDay: string) => {
    navigate(`/todos/${userClickDay}`);
  };

  return (
    <>
      <Container>
        <TitleBox>
          {!isMoon ? <div>날짜</div> : ''}
          <div>제목</div>
          <div>{isMoon ? '날짜' : '뽀모도로 개수'}</div>
        </TitleBox>
        <Line style={{ height: '2px' }} />
        {!isMoon && (
          <Content>
            {todoDetail.map((todo, index) => (
              <Text key={index} onClick={() => handleTodoPage(todo.task_date)}>
                <div>{todo.task_date}</div>
                <TextTitle>{todo.todo}</TextTitle>
                <div>
                  {todo.perform_count} / {todo.plan_count}
                </div>
              </Text>
            ))}
          </Content>
        )}
        {isMoon && (
          <Content>
            {moodDetail.map((mood) => (
              <Text
                key={mood.review_id}
                onClick={() => handleTodoPage(mood.task_date)}
              >
                <TextTitle>{mood.subject}</TextTitle>
                <div>{mood.task_date}</div>
              </Text>
            ))}
          </Content>
        )}
      </Container>
    </>
  );
};

export default Detail;

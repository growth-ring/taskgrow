import styled from 'styled-components';
import Button from '../UI/Button';

import { useTimerStore } from '../../store/timer';
import { useTodosStore } from '../../store/todos';
import { useReviewStore } from '../../store/review';
import resetTimer from '../../utils/resetTimer';

const Container = styled.div`
  margin-bottom: 30px;
`;

const Wrapper = styled.div`
  justify-content: space-between;
`;

const StateBtn = () => {
  const timer = useTimerStore();
  const todos = useTodosStore();
  const { openReview, closeReview } = useReviewStore();

  const handleShowTodo = () => {
    closeReview();
    timer.showTodo();
    resetTimer(timer, todos, 'reset', todos.todoList);
  };

  const handleShowBreak = () => {
    closeReview();
    timer.showBreak();
    resetTimer(timer, todos, '휴식');
    todos.setTodoId(0);
  };

  const handleShowReview = () => {
    openReview();
    timer.showReview();
    todos.setTodoId(0);
  };

  return (
    <Container>
      <Wrapper>
        <Button title="TODO" onClick={handleShowTodo}>
          할 일
        </Button>
        <Button title="BREAK" onClick={handleShowBreak}>
          휴식
        </Button>
        <Button title="REVIEW" onClick={handleShowReview}>
          회고
        </Button>
      </Wrapper>
    </Container>
  );
};

export default StateBtn;

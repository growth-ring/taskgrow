import styled from 'styled-components';
import Button from '../UI/Button';

import { useTimerStore } from '../../store/timer';
import { useTodosStore } from '../../store/todos';
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

  const handleShowTodo = () => {
    resetTimer(timer, todos, 'reset');
  };

  const handleShowBreak = () => {
    resetTimer(timer, todos, '휴식');
  };

  return (
    <Container>
      <Wrapper>
        <Button showTodo={timer.showTodoBtn} onClick={handleShowTodo}>
          할 일
        </Button>
        <Button showTodo={!timer.showTodoBtn} onClick={handleShowBreak}>
          휴식
        </Button>
      </Wrapper>
    </Container>
  );
};

export default StateBtn;

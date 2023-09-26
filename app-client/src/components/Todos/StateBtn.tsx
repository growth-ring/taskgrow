import styled from 'styled-components';
import Button from '../UI/Button';

import { useTimerStore } from '../../store/timer';
import { useTodosStore } from '../../store/todos';

const Container = styled.div`
  margin-bottom: 30px;
`;

const Wrapper = styled.div`
  justify-content: space-between;
`;

const StateBtn = () => {
  const { showTodoBtn, setShowTodoBtn, stop, setOnTimer, setTimerMinute } =
    useTimerStore();
  const { setSelectedTodo } = useTodosStore();

  const handleShowTodo = () => {
    stop();
    setShowTodoBtn(true);
    setSelectedTodo('오늘 할 일 골라주세요');
    setOnTimer(false);
    setTimerMinute(1);
  };

  const handleShowBreak = () => {
    stop();
    setShowTodoBtn(false);
    setSelectedTodo('휴식');
    setOnTimer(true);
    setTimerMinute(5);
  };

  return (
    <Container>
      <Wrapper>
        <Button showTodo={showTodoBtn} onClick={handleShowTodo}>
          할 일
        </Button>
        <Button showTodo={!showTodoBtn} onClick={handleShowBreak}>
          휴식
        </Button>
      </Wrapper>
    </Container>
  );
};

export default StateBtn;

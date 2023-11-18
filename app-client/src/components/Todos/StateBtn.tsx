import { useState } from 'react';
import styled from 'styled-components';
import Button from '../UI/Button';
import Alert from '../UI/Alert';

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
  const [isShow, setIsShow] = useState(false);
  const [title, setTitle] = useState('');

  const getIsShow = () => {
    setIsShow(!isShow);
  };

  const handleShowTodo = () => {
    if (timer.timerState === 'RUNNING' && timer.selectedBtn === 'BREAK') {
      setIsShow(true);
      setTitle('할 일');
    } else {
      closeReview();
      timer.showTodo();
      resetTimer(timer, todos, 'reset', todos.todoList);
    }
  };

  const handleShowBreak = () => {
    if (timer.timerState === 'RUNNING' && timer.selectedBtn === 'TODO') {
      setIsShow(true);
      setTitle('휴식');
    } else {
      closeReview();
      timer.showBreak();
      resetTimer(timer, todos, '휴식');
      todos.setTodoId(0);
    }
  };

  const handleShowReview = () => {
    if (timer.timerState === 'RUNNING') {
      setIsShow(true);
      setTitle('회고');
    } else {
      openReview();
      timer.showReview();
      todos.setTodoId(0);
    }
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
      {isShow && <Alert text={title} getIsShow={getIsShow} />}
    </Container>
  );
};

export default StateBtn;

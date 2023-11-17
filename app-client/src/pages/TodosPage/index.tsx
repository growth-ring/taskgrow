import { useState, useEffect } from 'react';
import Header from '../../components/Todos/Header';
import StateBtn from '../../components/Todos/StateBtn';
import Timer from '../../components/Pomodoro/Timer';
import Pomodoro from '../../components/Pomodoro/Pomodoro';
import PomodoroBtn from '../../components/Pomodoro/PomodoroBtn';
import TodoAllDelete from '../../components/Todos/TodoAllDelete';
import TodoList from '../../components/Todos/TodoList';
import Review from '../../components/Review/Review';
import { useTodosStore } from '../../store/todos';
import { useTimerStore } from '../../store/timer';
import { useReviewStore } from '../../store/review';
import { useTask } from '../../store/task';
import styled from 'styled-components';
import resetTimer from '../../utils/resetTimer';
import { updatePerformPomodoro } from '../../services/todo';
import { getTodos } from '../../services/todo';

const Container = styled.div`
  @media (max-width: 767px) {
    padding-bottom: 20px;
    margin-top: 20px;
  }

  @media (min-width: 768px) {
    display: flex;
    width: 100%;
    height: 90%;
  }
`;

const Todo = styled.div`
  margin: 0 10%;

  @media (max-width: 767px) {
    font-size: 20px;
  }

  @media (min-width: 768px) and (max-width: 1023px) {
    font-size: 25px;
  }

  @media (min-width: 1024px) {
    font-size: 30px;
  }
`;

const Line = styled.div`
  background-color: #f5f5f5;
`;

const Time = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  item-align: center;
  text-align: center;

  @media (min-width: 768px) {
    width: 50%;
  }
`;

const TodosBox = styled.div`
  @media (max-width: 767px) {
    margin: 10px 20px;
  }

  @media (min-width: 768px) {
    width: 50%;
    height: 100%;
    margin: 2.5rem;
  }
`;

const Todos = () => {
  const timer = useTimerStore();
  const todos = useTodosStore();
  const { selectedTaskId } = useTask();
  const { isReview, closeReview } = useReviewStore();
  const { onTimer, timerState, startTime, timerMinute, complete } =
    useTimerStore();
  const USER_TIME = timerMinute * 60 * 1000;
  const { todoId, selectedTodo, isTodoChange, setIsTodoChange } =
    useTodosStore();
  const [animationFrameId, setAnimationFrameId] = useState<number | null>(null);
  const [percentage, setPercentage] = useState<number>(0);
  const [timerTime, setTimerTime] = useState<number>(USER_TIME);
  const isBreak: boolean = selectedTodo === '휴식';

  useEffect(() => {
    getTodos(selectedTaskId).then((todoList) => {
      resetTimer(timer, todos, 'reset', todoList);
      closeReview();
    });
  }, [selectedTaskId, isTodoChange]);

  useEffect(() => {
    if (timerState === 'INITIAL') {
      setTimerTime(USER_TIME);
    }

    if (timerState !== 'RUNNING') {
      if (animationFrameId !== null) {
        cancelAnimationFrame(animationFrameId);
        setAnimationFrameId(null);
        setPercentage(0);
      }
      return;
    }

    const animate = () => {
      const currentTime = Date.now();
      const elapsedTime = currentTime - startTime;

      if (elapsedTime < USER_TIME) {
        setTimerTime(USER_TIME - elapsedTime);
        setPercentage((elapsedTime / USER_TIME) * 100);
        setAnimationFrameId(requestAnimationFrame(animate));
      } else {
        complete();
        updatePerformPomodoro(todoId).then(() =>
          setIsTodoChange(!isTodoChange),
        );
        setPercentage(100);
      }
    };

    if (animationFrameId === null) {
      setAnimationFrameId(requestAnimationFrame(animate));
    }

    return () => {
      if (animationFrameId !== null) {
        cancelAnimationFrame(animationFrameId);
      }
    };
  }, [timerState, animationFrameId, timerTime]);

  return (
    <>
      <Header />
      <Line style={{ height: '2px' }} />
      <Container>
        <Time>
          <StateBtn />
          {isReview && <Review />}
          {!isReview && (
            <>
              <Todo>{selectedTodo}</Todo>
              <Pomodoro percentage={percentage} timerState={timerState} />
              {onTimer && (
                <>
                  <Timer
                    time={timerTime}
                    timerState={timerState}
                    isBreak={isBreak}
                  />
                  <PomodoroBtn />
                </>
              )}
            </>
          )}
        </Time>
        <TodosBox>
          <TodoAllDelete />
          <TodoList />
        </TodosBox>
      </Container>
    </>
  );
};

export default Todos;

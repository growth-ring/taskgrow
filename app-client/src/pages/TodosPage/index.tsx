import { useState, useEffect } from 'react';
import Header from '../../components/Todos/Header';
import TaskTracking from '../../components/TaskTracking/TaskTracking';
import StateBtn from '../../components/Todos/StateBtn';
import Timer from '../../components/Pomodoro/Timer';
import Pomodoro from '../../components/Pomodoro/Pomodoro';
import PomodoroBtn from '../../components/Pomodoro/PomodoroBtn';
import AddTodo from '../../components/Todos/AddTodo';
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
  const [timerTime, setTimerTime] = useState<number>(USER_TIME);
  const isBreak: boolean = selectedTodo === '휴식';

  useEffect(() => {
    getTodos(selectedTaskId).then((todoList) => {
      resetTimer(timer, todos, 'reset', todoList);
      closeReview();
    });
  }, [selectedTaskId, isTodoChange]);

  useEffect(() => {
    setTimerTime(USER_TIME);
  }, [timerMinute, timerState]);

  useEffect(() => {
    let intervalId: any;

    if (timerState === 'RUNNING') {
      intervalId = setInterval(() => {
        const currentTime = Date.now();
        const elapsedTime = currentTime - startTime;

        if (elapsedTime >= USER_TIME) {
          complete();
          updatePerformPomodoro(todoId).then(() =>
            setIsTodoChange(!isTodoChange),
          );
        }
        setTimerTime(USER_TIME - elapsedTime);
      }, 500);
    }

    return () => {
      clearInterval(intervalId);
    };
  }, [timerState]);

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
              <Pomodoro
                startTime={startTime}
                userTime={USER_TIME}
                timerState={timerState}
              />
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
          <TaskTracking />
          <AddTodo />
          <TodoAllDelete />
          <TodoList />
        </TodosBox>
      </Container>
    </>
  );
};

export default Todos;

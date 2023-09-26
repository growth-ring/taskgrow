import { useState, useEffect } from 'react';
import styled from 'styled-components';
import { TimerState } from '../../store/timer';
import { useTimerStore } from '../../store/timer';
import { useTodosStore } from '../../store/todos';
import { updatePerformPomodoro } from '../../services/todo';
import { updateTodo } from '../../services/todo';

const Time = styled.div`
  color: var(--main-color);
  font-weight: bold;
  font-size: 40px;
  margin-bottom: 30px;
`;

interface TimerProps {
  time: number;
  timerState: TimerState;
}

const Timer = ({ time, timerState }: TimerProps) => {
  const USER_TIME = time * 60 * 1000;
  const INTERVAL = 1000;
  const [timerTime, setTimerTime] = useState<number>(USER_TIME);
  const { complete } = useTimerStore();
  const { planCount, performCount, todoId, selectedTodo } = useTodosStore();

  const minute = String(Math.floor((timerTime / (1000 * 60)) % 60)).padStart(
    2,
    '0',
  );
  const second = String(Math.floor((timerTime / 1000) % 60)).padStart(2, '0');

  useEffect(() => {
    if (timerState === 'INITIAL') {
      setTimerTime(USER_TIME);
    }

    if (timerState === 'RUNNING') {
      const timer = setInterval(() => {
        setTimerTime((prevTime) => prevTime - INTERVAL);
      }, INTERVAL);

      if (timerTime <= 0) {
        clearInterval(timer);
        complete();
        const pomodoroData = {
          todoId: todoId,
          performCount: performCount + 1,
        };
        updatePerformPomodoro(pomodoroData);

        if (performCount + 1 === planCount) {
          const todoData = {
            todoId: todoId,
            todo: selectedTodo,
            status: 'DONE',
            planCount: planCount,
          };
          updateTodo(todoData);
        } else {
          const todoData = {
            todoId: todoId,
            todo: selectedTodo,
            status: 'PROGRESS',
            planCount: planCount,
          };
          updateTodo(todoData);
        }
      }
      return () => {
        clearInterval(timer);
      };
    }
  }, [timerState, timerTime]);

  const isBreak: boolean = selectedTodo === '휴식';

  return (
    <>
      {!isBreak && timerState === 'INITIAL' && <Time> 25 : 00 </Time>}
      {isBreak && timerState === 'INITIAL' && <Time> 05 : 00 </Time>}
      {timerState === 'RUNNING' && (
        <Time>
          {minute} : {second}
        </Time>
      )}
      {timerState === 'FINISHED' && <Time> 종료 </Time>}
    </>
  );
};

export default Timer;

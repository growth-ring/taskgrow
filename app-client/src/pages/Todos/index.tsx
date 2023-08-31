import Header from '../../components/Todos/Header';
import StateBtn from '../../components/Todos/StateBtn';
import Timer from '../../components/Pomodoro/Timer';
import Pomodoro from '../../components/Pomodoro/Pomodoro';
import PomodoroBtn from '../../components/Pomodoro/PomodoroBtn';
import TodoList from '../../components/Todos/TodoList';
import { useTimerStore } from '../../store/timer';
import styled from 'styled-components';

const Container = styled.div`
  display: flex;
  width: 100%;
  height: 90%;
`;

const Todo = styled.div`
  font-size: 30px;
`;

const Line = styled.div`
  background-color: #f5f5f5;
`;

const Time = styled.div`
  width: 50%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  item-align: center;
  text-align: center;
`;

const Todos = () => {
  const { timerState, startTime, timerMinute } = useTimerStore();

  return (
    <>
      <Header />
      <Line style={{ height: '2px' }} />
      <Container>
        <Time>
          <StateBtn />
          <Todo>밥먹기</Todo>
          <Pomodoro
            startTime={startTime}
            timerState={timerState}
            time={timerMinute}
          />
          <Timer time={timerMinute} timerState={timerState}/>
          <PomodoroBtn />
        </Time>
        <div style={{ width: '50%' }}>
          <TodoList />
        </div>
      </Container>
    </>
  );
};

export default Todos;

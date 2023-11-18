import styled from 'styled-components';
import Button from '../UI/Button';
import isToday from '../../utils/isToday';
import { useTimerStore } from '../../store/timer';
import { useTodosStore } from '../../store/todos';

const CenteredBox = styled.div`
  display: flex;
  justify-content: center;
`;

const PomodoroBtn = () => {
  const { start, stop, timerState } = useTimerStore();
  const { taskDate } = useTodosStore();

  const handleStartClick = () => {
    if (isToday(taskDate)) {
      start();
    }
  };

  const handleStopClick = () => {
    stop();
  };

  return (
    <>
      {timerState === 'INITIAL' && (
        <CenteredBox>
          <Button onClick={handleStartClick}>시작</Button>
        </CenteredBox>
      )}
      {timerState === 'RUNNING' && (
        <CenteredBox>
          <Button onClick={handleStopClick}>정지</Button>
        </CenteredBox>
      )}
    </>
  );
};

export default PomodoroBtn;

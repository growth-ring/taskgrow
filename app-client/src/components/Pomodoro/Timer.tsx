import styled from 'styled-components';
import { TimerState } from '../../store/timer';

const Time = styled.div`
  color: var(--main-color);
  font-weight: bold;
  font-size: 40px;
  margin-bottom: 30px;
`;

interface TimerProps {
  time: number;
  timerState: TimerState;
  isBreak: boolean;
}

const Timer = ({ time, timerState, isBreak }: TimerProps) => {
  const minute = String(Math.floor((time / (1000 * 60)) % 60)).padStart(2, '0');
  const second = String(Math.floor((time / 1000) % 60)).padStart(2, '0');

  return (
    <>
      {!isBreak && timerState === 'INITIAL' && <Time> 25 : 00 </Time>}
      {isBreak && timerState === 'INITIAL' && <Time> 05 : 00 </Time>}
      {timerState === 'RUNNING' && (
        <Time>
          {minute} : {second}
        </Time>
      )}
    </>
  );
};

export default Timer;

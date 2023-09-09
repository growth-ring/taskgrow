import { useState, useEffect } from 'react';
import styled from 'styled-components';
import { TimerState } from '../../store/timer';

const Time = styled.div`
  color: var(--main-color);
  font-weight: bold;
  font-size: 40px;
  margin-bottom: 30px;
`

interface TimerProps {
  time: number;
  timerState: TimerState;
}

const Timer = ({ time, timerState }: TimerProps) => {
  const USER_TIME = time * 60 * 1000;
  const INTERVAL = 1000;
  const [timerTime, setTimerTime] = useState<number>(USER_TIME);

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
        console.log('타이머가 종료되었습니다.');
      }
      return () => {
        clearInterval(timer);
      };
    }
  }, [timerState, timerTime]);


  return (
    <>
      {timerState === 'INITIAL' && (
      <Time> 00 : 00</Time>
      )}
      {timerState === 'RUNNING' && (
        <Time>
          {minute} : {second}
        </Time>
      )}
    </>
  );
};

export default Timer;

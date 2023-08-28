import { useState, useEffect } from 'react';
import styled from 'styled-components';

const Time = styled.div`
  color: var(--main-color);
  font-weight: bold;
  font-size: 40px;
  margin-bottom: 30px;
`

interface TimerProps {
  time: number;
  isRunning: boolean;
  isTimer: boolean;
}

const Timer = ({ time, isRunning, isTimer }: TimerProps) => {
  const USER_TIME = time * 60 * 1000;
  const INTERVAL = 1000;
  const [timerTime, setTimerTime] = useState<number>(USER_TIME);

  const minute = String(Math.floor((timerTime / (1000 * 60)) % 60)).padStart(
    2,
    '0',
  );
  const second = String(Math.floor((timerTime / 1000) % 60)).padStart(2, '0');

  useEffect(() => {
    if (!isTimer) {
      setTimerTime(USER_TIME);
    }
    
    if (isRunning) {
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
  }, [isTimer, timerTime, isRunning]);


  return (
    <>
      {!isTimer && <Time> 00 : 00</Time>}
      {isTimer && (
        <Time>
          {minute} : {second}
        </Time>
      )}
    </>
  );
};

export default Timer;

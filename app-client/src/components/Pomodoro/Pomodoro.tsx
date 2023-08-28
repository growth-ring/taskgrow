import { useState, useEffect } from 'react';
import styled from 'styled-components';

interface PomodoroProps {
  time: number;
  isRunning: boolean;
  isTimer: boolean;
}

const CenteredBox = styled.div`
  display: flex;
  justify-content: center;
`

const DefaultPomodoro = styled.div`
  width: 300px;
  height: 300px;
  border-radius: 50%;
  background: var(--sub-yellow-color);
  margin: 30px;
`;

const Container = styled.div<{ percentage: number }>`
  width: 300px;
  height: 300px;
  border-radius: 50%;
  background: conic-gradient(var(--main-color) ${(props) => props.percentage}%, transparent ${(props) => props.percentage}%);
`;

const BackContainer = styled.div`
  width: 300px;
  height: 300px;
  border-radius: 50%;
  background: var(--sub-yellow-color);
  margin: 30px;
`

function Pomodoro({ time, isRunning, isTimer }: PomodoroProps) {
  const [percentage, setPercentage] = useState<number>(0);
  const [animationStartTime, setAnimationStartTime] = useState<number | null>(null);

  useEffect(() => {
    let animationFrameId: number | null = null;

    if(!isTimer) {
      //TODO: 무한루프 해결
      // setAnimationStartTime(null);
    }

    if (isRunning) {
      if (animationStartTime === null) {
        setAnimationStartTime(Date.now() - (percentage / 100) * (time * 60 * 1000));
      }

      const animationDuration: number = time * 60 * 1000;

      const animate = () => {
        const currentTime = Date.now();
        const elapsedTime = currentTime - (animationStartTime || currentTime);

        if (elapsedTime < animationDuration) {
          setPercentage((elapsedTime / animationDuration) * 100);
          animationFrameId = requestAnimationFrame(animate);
        } else {
          setPercentage(100);
        }
      };

      if (animationFrameId === null) {
        animationFrameId = requestAnimationFrame(animate);
      }
    } else {
      if (animationFrameId !== null) {
        cancelAnimationFrame(animationFrameId);
        animationFrameId = null;
      }
      if (animationStartTime !== null) {
        setAnimationStartTime(null);
      }
    }

    return () => {
      if (animationFrameId !== null) {
        cancelAnimationFrame(animationFrameId);
      }
    };
  }, [isTimer, time, isRunning, percentage, animationStartTime]);

  return (
    <>
    {!isTimer && <CenteredBox>
      <DefaultPomodoro /></CenteredBox>}
    {isTimer &&
    <CenteredBox><BackContainer><Container percentage={percentage} /></BackContainer></CenteredBox>
    }
  </>
  );
}

export default Pomodoro;
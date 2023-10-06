import { useState, useEffect } from 'react';
import styled from 'styled-components';
import { TimerState } from '../../store/timer';

const CenteredBox = styled.div`
  display: flex;
  justify-content: center;
`;

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
  background: conic-gradient(
    var(--main-color) ${(props) => props.percentage}%,
    transparent ${(props) => props.percentage}%
  );
`;

const BackContainer = styled.div`
  width: 300px;
  height: 300px;
  border-radius: 50%;
  background: var(--sub-yellow-color);
  margin: 30px;
`;

interface PomodoroProps {
  time: number;
  startTime: number;
  timerState: TimerState;
}

function Pomodoro({ timerState, time, startTime }: PomodoroProps) {
  const [percentage, setPercentage] = useState<number>(0);
  const [animationFrameId, setAnimationFrameId] = useState<number | null>(null);

  useEffect(() => {
    if (timerState !== 'RUNNING') {
      if (animationFrameId !== null) {
        cancelAnimationFrame(animationFrameId);
        setAnimationFrameId(null);
        setPercentage(0);
      }
      return;
    }

    const animationDuration: number = time * 60 * 1000;

    const animate = () => {
      const currentTime = Date.now();
      const elapsedTime = currentTime - startTime;

      if (elapsedTime < animationDuration) {
        setPercentage((elapsedTime / animationDuration) * 100);
        setAnimationFrameId(requestAnimationFrame(animate));
      } else {
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
  }, [timerState, animationFrameId]);

  return (
    <>
      {(timerState === 'INITIAL' || timerState === 'FINISHED') && (
        <CenteredBox>
          <DefaultPomodoro />
        </CenteredBox>
      )}
      {timerState === 'RUNNING' && (
        <CenteredBox>
          <BackContainer>
            <Container percentage={percentage} />
          </BackContainer>
        </CenteredBox>
      )}
    </>
  );
}

export default Pomodoro;

import { useEffect, useState } from 'react';

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
  startTime: number;
  userTime: number;
  timerState: TimerState;
}

function Pomodoro({ startTime, userTime, timerState }: PomodoroProps) {
  const [percentage, setPercentage] = useState(0);

  useEffect(() => {
    let animationId: any;

    if (timerState !== 'RUNNING') {
      return;
    }

    const animate = () => {
      const currentTime = Date.now();
      const elapsedTime = currentTime - startTime;

      setPercentage((elapsedTime / userTime) * 100);
      animationId = requestAnimationFrame(animate);
    };

    animationId = requestAnimationFrame(animate);

    return () => {
      if (animationId) {
        cancelAnimationFrame(animationId);
        setPercentage(0);
      }
    };
  }, [timerState]);

  return (
    <>
      {timerState === 'INITIAL' && (
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

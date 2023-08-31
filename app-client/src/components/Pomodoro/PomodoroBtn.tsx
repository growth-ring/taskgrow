import styled from 'styled-components';
import Button from '../UI/Button';
import { useTimerStore } from '../../store/timer';

const CenteredBox = styled.div`
display: flex;
justify-content: center;
`;

const PomodoroBtn = () => {
    const { start, stop, timerState } = useTimerStore();

    const handleStartClick = () => {
        start();
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

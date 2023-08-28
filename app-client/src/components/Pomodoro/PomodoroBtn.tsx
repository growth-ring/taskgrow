    import { useState } from 'react';
    import styled from 'styled-components';
    import Button from '../UI/Button';
    import { useTimerStore } from '../../store/timer';

    const CenteredBox = styled.div`
        display: flex;
        justify-content: center;
    `

    const PomodoroBtn = () => {
    const { setIsTimer, isRunning, setIsRunning, setTimerMinute } = useTimerStore();
    const [isStart, setIsStart] = useState(true);

    const handleStartClick = () => {
        setIsStart(false);
        setIsTimer(true);
    };

    const handleStopClick = () => {
        setIsRunning(!isRunning);
    };

    const handleResetClick = () => {
        setIsRunning(true);
        setIsStart(true);
        setIsTimer(false);
        setTimerMinute(1);
    };

    return (
        <>
        {isStart && <CenteredBox><Button onClick={handleStartClick}>시작</Button></CenteredBox>}
        {!isStart && (
            <CenteredBox>
                <Button onClick={handleResetClick}>초기화</Button>
                <Button onClick={handleStopClick}>{isRunning ? "일시정지" : "시작"}</Button>
            </CenteredBox>
        )}
        </>
    );
    };

    export default PomodoroBtn;

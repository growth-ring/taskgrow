import styled from 'styled-components';

interface AddTodoTimerCountProps {
  fillTimer: number;
  onClick: (count: number) => void;
}

const TimerCount = styled.input`
  width: 40px;
  text-align: center;
`;

const AddTodoTimerCount = ({ fillTimer, onClick }: AddTodoTimerCountProps) => {
  const handleTimerCountChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    onClick(Number(e.target.value));
  };

  return (
    <TimerCount
      type="number"
      value={fillTimer}
      onChange={handleTimerCountChange}
      min="1"
      max="20"
      required
    />
  );
};

export default AddTodoTimerCount;

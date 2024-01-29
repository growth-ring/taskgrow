import styled from 'styled-components';
import { IoTime } from 'react-icons/io5';

const Button = styled.button<{ $isTimerClick: boolean }>`
  font-size: 20px;
  color: ${(props) => (props.$isTimerClick ? 'var(--main-color)' : 'gray')};
`;

interface AddTodoTimerProps {
  customKey: number;
  isTimerClick: boolean;
  onClick: () => void;
}

const AddTodoTimer = ({
  customKey,
  isTimerClick,
  onClick,
}: AddTodoTimerProps) => {
  return (
    <Button key={customKey} onClick={onClick} $isTimerClick={isTimerClick}>
      <IoTime />
    </Button>
  );
};

export default AddTodoTimer;

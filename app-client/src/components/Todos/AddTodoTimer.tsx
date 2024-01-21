import styled from 'styled-components';
import { IoTime } from 'react-icons/io5';

const Button = styled.button<{ $isTimerClick: boolean }>`
  font-size: 20px;
  color: ${(props) => (props.$isTimerClick ? 'var(--main-color)' : 'gray')};
`;

interface AddTodoTimerProps {
  key: number;
  isTimerClick: boolean;
  onClick: () => void;
}

const AddTodoTimer = ({ key, isTimerClick, onClick }: AddTodoTimerProps) => {
  return (
    <Button key={key} onClick={onClick} $isTimerClick={isTimerClick}>
      <IoTime />
    </Button>
  );
};

export default AddTodoTimer;

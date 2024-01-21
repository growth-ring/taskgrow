import { useState, useEffect } from 'react';
import AddTodoTimer from './AddTodoTimer';
import AddTodoTimerCount from './AddTodoTimerCount';

interface AddTodoTimerProps {
  count: number;
  planCount: string;
  handleTodoTimerChange: (count: string) => void;
}

const AddTodoTimerList = ({
  count,
  planCount,
  handleTodoTimerChange,
}: AddTodoTimerProps) => {
  const [fillTimer, setFillTimer] = useState(+planCount);
  const todoTimers = [];

  const handleTimerClick = (clickedKey: number) => {
    setFillTimer(clickedKey);
    handleTodoTimerChange(clickedKey.toString());
  };

  useEffect(() => {
    setFillTimer(+planCount);
  }, [planCount]);

  for (let i = 1; i <= count; i++) {
    todoTimers.push(
      <AddTodoTimer
        key={i}
        isTimerClick={fillTimer >= i}
        onClick={() => handleTimerClick(i)}
      />,
    );
  }
  todoTimers.push(
    <AddTodoTimerCount fillTimer={fillTimer} onClick={handleTimerClick} />,
  );
  return todoTimers;
};

export default AddTodoTimerList;

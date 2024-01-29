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

  const handleTimerClick = (clickedKey: number) => {
    setFillTimer(clickedKey);
    handleTodoTimerChange(clickedKey.toString());
  };

  useEffect(() => {
    setFillTimer(+planCount);
  }, [planCount]);

  const todoTimers = Array.from({ length: count }, (_, index) => (
    <AddTodoTimer
      key={index + 1}
      customKey={index + 1}
      isTimerClick={fillTimer >= index + 1}
      onClick={() => handleTimerClick(index + 1)}
    />
  ));

  todoTimers.push(
    <AddTodoTimerCount
      key="count"
      fillTimer={fillTimer}
      onClick={handleTimerClick}
    />,
  );
  return todoTimers;
};

export default AddTodoTimerList;

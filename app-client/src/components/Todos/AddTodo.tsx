import styled from 'styled-components';
import { useState } from 'react';
import { useTask } from '../../store/task';
import { addTodo } from '../../services/todo';
import { SlPlus } from 'react-icons/sl';
import { useTodosStore } from '../../store/todos';
import AddTodoTimerList from './AddTodoTimerList';
import { isGuest } from '../../utils/isGuest';
import { useGuestStore } from '../../store/guest';

const Button = styled.button`
  color: gray;
`;

const Form = styled.form`
  margin: 0 10px;
  width: 100%;
`;

const Input = styled.input`
  width: 100%;
`;

const AddTodo = () => {
  const { isTodoChange, setIsTodoChange } = useTodosStore();
  const { selectedTaskId } = useTask();
  const { guestAddTodo, todoListId, incrementTodoListId } = useGuestStore();
  const [todo, setTodo] = useState('');
  const [planCount, setPlanCount] = useState('1');

  const handleAddTodo = async (e: React.SyntheticEvent) => {
    e.preventDefault();

    if (+planCount > 0 && +planCount <= 20) {
      if (isGuest()) {
        const newTodo = {
          todo: todo,
          status: 'READY',
          performCount: 0,
          planCount: +planCount,
          todoId: todoListId,
          taskId: selectedTaskId,
        };
        incrementTodoListId();
        guestAddTodo(newTodo);
      } else {
        await addTodo({
          taskId: selectedTaskId,
          todo: todo,
          performCount: 0,
          planCount: +planCount,
        });
      }
      setIsTodoChange(!isTodoChange);
      setTodo('');
      setPlanCount('1');
    } else if (+planCount <= 0) {
      alert('1 이상의 숫자를 넣어주세요');
    } else {
      alert('뽀모도로는 최대 20개까지 가능합니다');
    }
  };

  const handleTodoChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setTodo(e.target.value);
  };

  const handleTodoTimerChange = (count: string) => {
    setPlanCount(count);
  };

  return (
    <div className="max-w-lg mx-auto bg-white p-4 rounded-lg shadow shadow-slate-300 flex">
      <Button onClick={handleAddTodo}>
        <SlPlus />
      </Button>
      <Form onSubmit={handleAddTodo}>
        <Input
          type="text"
          value={todo}
          onChange={handleTodoChange}
          maxLength={50}
          placeholder="할 일을 추가하고 엔터키를 누르세요"
          required
        />
      </Form>
      <AddTodoTimerList
        count={5}
        planCount={planCount}
        handleTodoTimerChange={handleTodoTimerChange}
      />
    </div>
  );
};

export default AddTodo;

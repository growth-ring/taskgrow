import { useState } from 'react';
import {
  BsPencil,
  BsTrash3,
  BsCheckSquareFill,
  BsSquare,
} from 'react-icons/bs';
import TodoDetail from './TodoDetail';
import DeleteTodo from './DeleteTodo';
import { updateTodo } from '../../services/todo';
import { useTodosStore } from '../../store/todos';
import { useTimerStore } from '../../store/timer';
import resetTimer from '../../utils/resetTimer';
import { isGuest } from '../../utils/isGuest';
import { useGuestStore } from '../../store/guest';

interface TodoProps {
  id: number;
  title: string;
  status: string;
  planCount: number;
  performCount: number;
  onClick: () => void;
}

const Todo = ({
  id,
  title,
  status,
  planCount,
  performCount,
  onClick,
}: TodoProps) => {
  const timer = useTimerStore();
  const todos = useTodosStore();
  const { isTodoChange, setIsTodoChange } = useTodosStore();
  const { updateTodoStatus } = useGuestStore();

  const [isDetailShow, setIsDetailShow] = useState(false);
  const [isDeleteShow, setIsDeleteShow] = useState(false);

  const getIsDetailShow = () => {
    setIsDetailShow(false);
  };

  const getIsDeleteShow = () => {
    setIsDeleteShow(false);
  };

  const handleTodoDelete = () => {
    setIsDeleteShow(true);
  };

  const handleTodoDetail = () => {
    setIsDetailShow(true);
  };

  const handleTodoComplete = async () => {
    if (status === 'READY') {
      alert('진행한 Todo만 완료할 수 있습니다');
    } else {
      if (isGuest()) {
        const nowStatus = status === 'PROGRESS' ? 'DONE' : 'PROGRESS';
        updateTodoStatus(id, nowStatus);
      } else {
        const todoData = {
          todoId: id,
          todo: title,
          status: status === 'PROGRESS' ? 'DONE' : 'PROGRESS',
          planCount: planCount,
        };
        await updateTodo(todoData);
      }
      setIsTodoChange(!isTodoChange);
      resetTimer(timer, todos, 'reset');
    }
  };

  return (
    <>
      <div
        className={`flex justify-between items-center ${
          status === 'DONE' ? 'text-darkGray' : ''
        }`}
        style={{
          border: todos.todoId === id ? '2.5px solid var(--main-color)' : '',
          borderRadius: '0.5rem',
          padding:
            todos.todoId === id ? '0.8rem 0 0.8rem 0.8rem' : '1rem 0 1rem 1rem',
        }}
      >
        <div className="inline-flex w-3/5">
          <button onClick={handleTodoComplete} style={{ width: '10%' }}>
            {status === 'DONE' ? (
              <BsCheckSquareFill style={{ color: 'var(--main-color)' }} />
            ) : (
              <BsSquare />
            )}
          </button>
          <div
            style={{
              width: '100%',
              color: todos.todoId === id ? 'var(--main-color)' : '',
            }}
          >
            <button
              onClick={onClick}
              style={{
                textAlign: 'left',
                textDecoration: status === 'DONE' ? 'line-through' : '',
              }}
            >
              {title}
            </button>
          </div>
        </div>
        <div style={{ display: 'flex' }}>
          <div className={`px-5 pl-0`}>
            {performCount} / {planCount}
          </div>
          <button onClick={handleTodoDelete}>
            <BsTrash3 />
          </button>
          <button
            onClick={handleTodoDetail}
            disabled={status === 'DONE'}
            style={{
              padding: todos.todoId === id ? '0 0.3rem 0 0.5rem' : '0 0.5rem',
            }}
          >
            <BsPencil />
          </button>
        </div>
      </div>
      {isDetailShow && (
        <TodoDetail
          todoId={id}
          todoTitle={title}
          todoStatus={status}
          todoPlanCount={planCount}
          todoPerformCount={performCount}
          getIsShow={getIsDetailShow}
        />
      )}
      {isDeleteShow && (
        <DeleteTodo todoId={id} todoTitle={title} getIsShow={getIsDeleteShow} />
      )}
    </>
  );
};
export default Todo;

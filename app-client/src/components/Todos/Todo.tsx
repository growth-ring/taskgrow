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
        className={`flex justify-between items-center border-b border-slate-200 py-3 border-l-4 ${
          status === 'DONE' ? 'border-l-transparent' : 'border-l-indigo-300'
        } ${
          status === 'DONE'
            ? 'color-main-colorbg-gradient-to-r from-transparent to-transparent hover:from-slate-100'
            : 'bg-gradient-to-r from-indigo-100 to-transparent hover:from-indigo-200'
        } transition ease-linear duration-150 cursor-pointer`}
        style={{ display: 'flex', width: '100%' }}
      >
        <div
          className="inline-flex items-center space-x-2"
          style={{ width: '60%' }}
        >
          <button onClick={handleTodoComplete} style={{ width: '10%' }}>
            {status === 'DONE' ? (
              <BsCheckSquareFill style={{ color: 'var(--main-color)' }} />
            ) : (
              <BsSquare />
            )}
          </button>
          <div
            className={`${status === 'DONE' ? 'line-through' : ''}`}
            style={{
              width: '100%',
              color: todos.todoId === id ? 'var(--main-color)' : '',
            }}
          >
            <button onClick={onClick} style={{ textAlign: 'left' }}>
              {title}
            </button>
          </div>
        </div>
        <div style={{ display: 'flex' }}>
          <div className={`text-slate-500 px-5`}>
            {performCount} / {planCount}
          </div>
          <button onClick={handleTodoDelete}>
            <BsTrash3 />
          </button>
          <button
            className={`text-slate-500 px-2`}
            onClick={handleTodoDetail}
            disabled={status === 'DONE'}
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

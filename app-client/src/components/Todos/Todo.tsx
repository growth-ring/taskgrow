import { useState } from 'react';
import { AiOutlineUnorderedList } from 'react-icons/ai';
import TodoDetail from './TodoDetail';

interface TodoProps {
  title: string;
  status: string;
  planCount: number;
  performCount: number;
  onClick: () => void;
}

const Todo = ({
  title,
  status,
  planCount,
  performCount,
  onClick,
}: TodoProps) => {
  const [isShow, setIsShow] = useState(false);

  const getIsShow = () => {
    setIsShow(false);
  };

  const handleTodoDetail = () => {
    setIsShow(true);
  };

  return (
    <>
      <div
        className={`flex justify-between items-center border-b border-slate-200 py-3 border-l-4 ${
          status === 'done' ? 'border-l-transparent' : 'border-l-indigo-300'
        } ${
          status === 'done'
            ? 'color-main-colorbg-gradient-to-r from-transparent to-transparent hover:from-slate-100'
            : 'bg-gradient-to-r from-indigo-100 to-transparent hover:from-indigo-200'
        } transition ease-linear duration-150 cursor-pointer`}
        onClick={onClick}
      >
        <div className="inline-flex items-center space-x-2">
          <div>
            <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              strokeWidth="1.5"
              stroke={status === 'done' ? 'var(--main-color)' : 'currentColor'}
              className="w-6 h-6 text-slate-500"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                d="M4.5 12.75l6 6 9-13.5"
              />
            </svg>
          </div>
          <div
            className={`text-slate-500 ${
              status === 'done' ? 'line-through' : ''
            }`}
          >
            {title}
          </div>
        </div>
        <div style={{ display: 'flex' }}>
          <div className={`text-slate-500 px-5`}>
            {performCount} / {planCount}
          </div>
          <button>
            <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              strokeWidth="1.5"
              stroke="currentColor"
              className="w-4 h-4 text-slate-500 hover:text-slate-700 hover:cursor-pointer"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                d="M14.74 9l-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 01-2.244 2.077H8.084a2.25 2.25 0 01-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 00-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 013.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 00-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 00-7.5 0"
              />
            </svg>
          </button>
          <button className={`text-slate-500 px-2`} onClick={handleTodoDetail}>
            <AiOutlineUnorderedList />
          </button>
        </div>
      </div>
      {isShow && (
        <TodoDetail
          todoTitle={title}
          todoStatus={status}
          todoPlanCount={planCount}
          todoPerformCount={performCount}
          getIsShow={getIsShow}
        />
      )}
    </>
  );
};
export default Todo;

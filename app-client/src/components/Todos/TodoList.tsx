import Todo from './Todo';
import { useRef } from 'react';
import { useTodosStore } from '../../store/todos';
import { useTimerStore } from '../../store/timer';
import { useReviewStore } from '../../store/review';
import resetTimer from '../../utils/resetTimer';
import { updateTodoOrder } from '../../services/todo';
import { todosDragUpdateOrderNo } from '../../utils/todosUpdateOrderNo';
import { useTask } from '../../store/task';

interface Todo {
  todoId: number;
  todo: string;
  status: string;
  planCount: number;
  performCount: number;
}

const TodoList = () => {
  const { closeReview } = useReviewStore();
  const timer = useTimerStore();
  const todos = useTodosStore();
  const {
    setPlanCount,
    todoList,
    setTodoId,
    setPerformCount,
    setIsTodoChange,
    isTodoChange,
  } = useTodosStore();
  const { selectedTaskId } = useTask();

  const handleTodoClick = (todo: Todo) => {
    if (timer.timerState === 'RUNNING') {
      return alert('현재 타이머가 진행 중입니다. 정지 후 선택해주세요.');
    }

    if (todo.status !== 'DONE') {
      setTodoId(todo.todoId);
      localStorage.setItem('todoId', String(todo.todoId));
      setPlanCount(todo.planCount);
      setPerformCount(todo.performCount);
      resetTimer(timer, todos, todo.todo);
      closeReview();
    }
  };

  const dragItemOrderNo = useRef<number>(0);
  const dragItemTodoId = useRef<number>(0);
  const dragOverItemOrderNo = useRef<number>(0);
  const dragOverItemTodoId = useRef<number>(0);

  const handleDragStart = (orderNo: number, todoId: number) => {
    dragItemOrderNo.current = orderNo;
    dragItemTodoId.current = todoId;
  };

  const handleDragEnter = (orderNo: number, todoId: number) => {
    dragOverItemOrderNo.current = orderNo;
    dragOverItemTodoId.current = todoId;
  };

  const drop = async (e: React.DragEvent<HTMLLIElement>) => {
    const x = e.clientX;
    const y = e.clientY;

    const tasksElement = document.getElementById('task');
    const rect = tasksElement!.getBoundingClientRect();

    const top = rect.top;
    const bottom = rect.bottom;
    const left = rect.left;
    const right = rect.right;

    if (x >= left && x <= right && y >= top && y <= bottom) {
      await todosDragUpdateOrderNo(
        selectedTaskId,
        dragItemOrderNo.current,
        dragOverItemOrderNo.current,
      );
      await updateTodoOrder(
        dragItemTodoId.current,
        dragOverItemOrderNo.current,
      );
      setIsTodoChange(!isTodoChange);
    }
  };

  return (
    <div
      className="max-w-lg mx-auto bg-white rounded-xl shadow shadow-slate-300"
      style={{ width: '100%' }}
    >
      <ul
        id="task"
        style={{
          width: '100%',
          height: '100%',
          overflow: 'auto',
        }}
      >
        {todoList
          .slice()
          .sort((a, b) => {
            if (a.status !== 'DONE' && b.status !== 'DONE') {
              return b.orderNo! - a.orderNo!;
            } else if (a.status === 'DONE') {
              return 1;
            } else {
              return -1;
            }
          })
          .map((todo) => (
            <li
              key={todo.todoId}
              onDragStart={() => handleDragStart(todo.orderNo, todo.todoId)}
              onDragEnter={() => handleDragEnter(todo.orderNo, todo.todoId)}
              onDragEnd={(e) => drop(e)}
              draggable
            >
              <Todo
                key={todo.todoId}
                id={todo.todoId}
                title={todo.todo}
                status={todo.status}
                planCount={todo.planCount}
                performCount={todo.performCount}
                onClick={() => handleTodoClick(todo)}
              />
            </li>
          ))}
      </ul>
    </div>
  );
};

export default TodoList;

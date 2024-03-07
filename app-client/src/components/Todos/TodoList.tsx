import Todo from './Todo';
import styled from 'styled-components';
import { useRef, useEffect, useState } from 'react';
import { useTodosStore } from '../../store/todos';
import { useTimerStore } from '../../store/timer';
import { useReviewStore } from '../../store/review';
import resetTimer from '../../utils/resetTimer';
import { updateTodoOrder } from '../../services/todo';
import {
  guestTodosDragUpdateOrderNo,
  todosDragUpdateOrderNo,
} from '../../utils/todosUpdateOrderNo';
import { useTask } from '../../store/task';
import { isGuest } from '../../utils/isGuest';
import { useGuestStore } from '../../store/guest';

interface Todo {
  todoId: number;
  todo: string;
  status: string;
  planCount: number;
  performCount: number;
  category: string | number | null;
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
  const { guestTodoList, updateTodoOrderNo } = useGuestStore();
  const [categories, setCategories] = useState<any[]>([]);

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

  const drop = async (
    e: React.DragEvent<HTMLLIElement>,
    category?: string | number | null,
  ) => {
    const x = e.clientX;
    const y = e.clientY;
    const id = category ? category?.toString() : 'task';

    const tasksElement = document.getElementById(id);
    const rect = tasksElement!.getBoundingClientRect();

    const top = rect.top;
    const bottom = rect.bottom;
    const left = rect.left;
    const right = rect.right;

    if (x >= left && x <= right && y >= top && y <= bottom) {
      if (isGuest()) {
        await guestTodosDragUpdateOrderNo(
          guestTodoList,
          dragItemOrderNo.current,
          dragOverItemOrderNo.current,
          updateTodoOrderNo,
        );
        await updateTodoOrderNo(
          dragItemTodoId.current,
          dragOverItemOrderNo.current,
        );
      } else {
        await todosDragUpdateOrderNo(
          selectedTaskId,
          dragItemOrderNo.current,
          dragOverItemOrderNo.current,
        );
        await updateTodoOrder(
          dragItemTodoId.current,
          dragOverItemOrderNo.current,
        );
      }
      setIsTodoChange(!isTodoChange);
    }
  };

  useEffect(() => {
    const category = Array.from(new Set(todoList.map((todo) => todo.category)));
    if (!category.includes(null)) {
      category.push(null);
    }
    setCategories(category);
  }, [todos.isCategory, isTodoChange, todoList]);

  return (
    <div
      className="max-w-lg mx-auto bg-white rounded-xl shadow shadow-slate-300"
      style={{ width: '100%' }}
    >
      {todos.isCategory && (
        <>
          {categories.map((category) => (
            <ul
              id={category}
              style={{
                width: '100%',
                height: '100%',
                overflow: 'auto',
              }}
            >
              <Category>
                {category === null ? '카테고리 없음' : category}
              </Category>
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
                  <>
                    {todo.category === category && (
                      <li
                        key={todo.todoId}
                        onDragStart={() =>
                          handleDragStart(todo.orderNo, todo.todoId)
                        }
                        onDragEnter={() =>
                          handleDragEnter(todo.orderNo, todo.todoId)
                        }
                        onDragEnd={(e) => drop(e, category)}
                        draggable
                      >
                        <Todo
                          key={todo.todoId}
                          id={todo.todoId}
                          title={todo.todo}
                          status={todo.status}
                          planCount={todo.planCount}
                          performCount={todo.performCount}
                          category={todo.category}
                          onClick={() => handleTodoClick(todo)}
                        />
                      </li>
                    )}
                  </>
                ))}
            </ul>
          ))}
        </>
      )}

      {!todos.isCategory && (
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
              <>
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
                    category={todo.category}
                    onClick={() => handleTodoClick(todo)}
                  />
                </li>
              </>
            ))}
        </ul>
      )}
    </div>
  );
};

export default TodoList;

const Category = styled.div`
  padding: 10px;
  background-color: var(--sub-yellow-color);
`;

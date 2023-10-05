import { useEffect } from 'react';
import Todo from './Todo';
import { useTodosStore } from '../../store/todos';
import { useTimerStore } from '../../store/timer';
import { useTask } from '../../store/task';
import { getTodos } from '../../services/todo';

interface Todo {
  todo_id: number;
  todo: string;
  status: string;
  plan_count: number;
  perform_count: number;
}

const TodoList = () => {
  const { selectedTaskId } = useTask();
  const {
    setPlanCount,
    todoList,
    setTodoList,
    setTodoId,
    setSelectedTodo,
    setPerformCount,
    isTodoChange,
  } = useTodosStore();
  const { setShowTodoBtn, setOnTimer, setTimerMinute, stop } = useTimerStore();

  const handleTodoClick = (todo: Todo) => {
    if (todo.status === 'DONE') {
      alert('완료된 할 일 입니다.');
    } else {
      setOnTimer(true);
      setShowTodoBtn(true);
      setTodoId(todo.todo_id);
      setPlanCount(todo.plan_count);
      setPerformCount(todo.perform_count);
      setSelectedTodo(todo.todo);
      setTimerMinute(25);
      stop();
    }
  };

  useEffect(() => {
    getTodos(selectedTaskId).then((todos) => setTodoList(todos));
  }, [selectedTaskId, isTodoChange]);

  return (
    <div
      className="max-w-lg mx-auto bg-white p-8 rounded-xl shadow shadow-slate-300"
      style={{ width: '100%' }}
    >
      <div
        id="tasks"
        style={{ width: '100%', height: '100%', overflow: 'auto' }}
      >
        {todoList
          .slice()
          .sort((a) => (a.status === 'DONE' ? 1 : -1))
          .map((todo) => (
            <Todo
              key={todo.todo_id}
              id={todo.todo_id}
              title={todo.todo}
              status={todo.status}
              planCount={todo.plan_count}
              performCount={todo.perform_count}
              onClick={() => handleTodoClick(todo)}
            />
          ))}
      </div>
    </div>
  );
};

export default TodoList;

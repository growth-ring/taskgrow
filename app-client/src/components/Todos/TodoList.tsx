import { useEffect } from 'react';
import Todo from './Todo';
import { useTodosStore } from '../../store/todos';
import { useTimerStore } from '../../store/timer';
import { useTask } from '../../store/task';
import { getTodos } from '../../services/todo';

const TodoList = () => {
  const { selectedTaskId } = useTask();
  const { todoList, setTodoList, setSelectedTodo } = useTodosStore();
  const { setShowTodoBtn, setOnTimer } = useTimerStore();

  const handleTodoClick = (title: string) => {
    setOnTimer(true);
    setShowTodoBtn(true);
    setSelectedTodo(title);
  };

  useEffect(() => {
    getTodos(selectedTaskId).then((todos) => setTodoList(todos));
  }, [selectedTaskId]);

  return (
    <div
      className="max-w-lg mx-auto bg-white p-8 rounded-xl shadow shadow-slate-300"
      style={{ width: '100%' }}
    >
      <div
        id="tasks"
        style={{ width: '100%', height: '100%', overflow: 'auto' }}
      >
        {todoList.map((todo) => (
          <Todo
            key={todo.todo_id}
            title={todo.todo}
            status={todo.status}
            planCount={todo.plan_count}
            performCount={todo.perform_count}
            onClick={() => handleTodoClick(todo.todo)}
          />
        ))}
      </div>
    </div>
  );
};

export default TodoList;

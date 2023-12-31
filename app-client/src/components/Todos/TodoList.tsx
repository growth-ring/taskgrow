import Todo from './Todo';
import { useTodosStore } from '../../store/todos';
import { useTimerStore } from '../../store/timer';
import { useReviewStore } from '../../store/review';
import resetTimer from '../../utils/resetTimer';

interface Todo {
  todo_id: number;
  todo: string;
  status: string;
  plan_count: number;
  perform_count: number;
}

const TodoList = () => {
  const { closeReview } = useReviewStore();
  const timer = useTimerStore();
  const todos = useTodosStore();
  const { setPlanCount, todoList, setTodoId, setPerformCount } =
    useTodosStore();

  const handleTodoClick = (todo: Todo) => {
    if (todo.status !== 'DONE') {
      setTodoId(todo.todo_id);
      localStorage.setItem('todoId', String(todo.todo_id));
      setPlanCount(todo.plan_count);
      setPerformCount(todo.perform_count);
      resetTimer(timer, todos, todo.todo);
      closeReview();
    }
  };

  return (
    <div
      className="max-w-lg mx-auto bg-white p-4 rounded-xl shadow shadow-slate-300"
      style={{ width: '100%' }}
    >
      <div
        id="tasks"
        style={{
          width: '100%',
          height: '100%',
          overflow: 'auto',
        }}
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

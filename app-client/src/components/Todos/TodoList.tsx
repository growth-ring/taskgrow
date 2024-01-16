import Todo from './Todo';
import { useTodosStore } from '../../store/todos';
import { useTimerStore } from '../../store/timer';
import { useReviewStore } from '../../store/review';
import resetTimer from '../../utils/resetTimer';

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
  const { setPlanCount, todoList, setTodoId, setPerformCount } =
    useTodosStore();

  const handleTodoClick = (todo: Todo) => {
    if (todo.status !== 'DONE') {
      setTodoId(todo.todoId);
      localStorage.setItem('todoId', String(todo.todoId));
      setPlanCount(todo.planCount);
      setPerformCount(todo.performCount);
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
              key={todo.todoId}
              id={todo.todoId}
              title={todo.todo}
              status={todo.status}
              planCount={todo.planCount}
              performCount={todo.performCount}
              onClick={() => handleTodoClick(todo)}
            />
          ))}
      </div>
    </div>
  );
};

export default TodoList;

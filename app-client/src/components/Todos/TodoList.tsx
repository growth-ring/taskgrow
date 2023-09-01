import Todo from './Todo';
import { useTodosStore } from '../../store/todos';
import { useTimerStore } from '../../store/timer';

const TodoList = () => {
  const { setSelectedTodo } = useTodosStore();
  const { setOnTimer, stop } = useTimerStore();

  const handleTodoClick = (title: string) => {
    setOnTimer(true);
    setSelectedTodo(title);
    stop();
  };

  return (
    <div className="max-w-lg mx-auto my-10 bg-white p-8 rounded-xl shadow shadow-slate-300" style={{ width: "100%" }}>
      <div id="tasks" style={{ width: "100%", height: "100%", overflow: "auto"}}>
        <Todo
          title="밥먹기"
          completed={true}
          planCount={3}
          performCount={3}
          onClick={() => handleTodoClick("밥먹기")}
        />
        <Todo
          title="영단어 외우기"
          completed={false}
          planCount = {2}
          performCount = {1}
          onClick={() => handleTodoClick("영단어 외우기")}
        />
      </div>
    </div>
  );
};

export default TodoList;

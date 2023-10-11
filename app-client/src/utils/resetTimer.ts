import { TimerStore } from '../store/timer';
import { TodosStore } from '../store/todos';

function resetTimer(timer: TimerStore, todos: TodosStore, todo: string): void {
  const { setShowTodoBtn, setOnTimer, stop, setTimerMinute } = timer;
  const { setSelectedTodo } = todos;

  stop();
  if (todo === 'reset') {
    setSelectedTodo('오늘 할 일 골라주세요');
    setOnTimer(false);
    setShowTodoBtn(true);
    setTimerMinute(25);
  } else {
    setSelectedTodo(todo);
    setOnTimer(true);
    if (todo === '휴식') {
      setShowTodoBtn(false);
      setTimerMinute(5);
    } else {
      setShowTodoBtn(true);
      setTimerMinute(25);
    }
  }
}

export default resetTimer;

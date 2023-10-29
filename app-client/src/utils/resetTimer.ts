import { TimerStore } from '../store/timer';
import { TodosStore } from '../store/todos';

function resetTimer(timer: TimerStore, todos: TodosStore, todo: string): void {
  const { showTodo, showBreak, setOnTimer, stop, setTimerMinute } = timer;
  const { setSelectedTodo } = todos;

  stop();
  if (todo === 'reset') {
    setSelectedTodo('오늘 할 일 골라주세요');
    setOnTimer(false);
    showTodo();
    setTimerMinute(25);
  } else {
    setSelectedTodo(todo);
    setOnTimer(true);
    if (todo === '휴식') {
      showBreak();
      setTimerMinute(5);
    } else {
      showTodo();
      setTimerMinute(25);
    }
  }
}

export default resetTimer;

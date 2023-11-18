import { TimerStore } from '../store/timer';
import { TodosStore } from '../store/todos';
import { Todo } from '../store/todos';

function resetTimer(
  timer: TimerStore,
  todos: TodosStore,
  todo: string,
  todoList?: Todo[],
): void {
  const { timerState, showTodo, showBreak, setOnTimer, stop, setTimerMinute } =
    timer;
  const { setSelectedTodo, setTodoList, setTodoId } = todos;

  if (todoList) {
    setTodoList(todoList);
  }

  if (todo === 'reset') {
    if (timerState !== 'FINISHED') {
      setTodoId(0);
      stop();
      setOnTimer(false);
      setSelectedTodo(
        todoList?.length ? '오늘 할 일 골라주세요' : '오늘 할 일 추가해 주세요',
      );
    }
    showTodo();
    setTimerMinute(25);
  } else {
    stop();
    setSelectedTodo(todo);
    setOnTimer(true);
    todo === '휴식' ? showBreak() : showTodo();
    setTimerMinute(todo === '휴식' ? 5 : 25);
  }
}

export default resetTimer;

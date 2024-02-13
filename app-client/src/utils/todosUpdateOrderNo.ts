import { getTodos } from '../services/todo';
import { updateTodoOrder } from '../services/todo';

interface TodoData {
  todoId: number;
  taskId: number;
  todo: string;
  status: string;
  performCount: number;
  planCount: number;
  orderNo: number;
}

export const todosUpdateOrderNo = async (
  selectedTaskId: number,
  todoId: number,
) => {
  // 해당 task의 모든 todo를 불러옴
  const todos: TodoData[] = await getTodos(selectedTaskId);

  // 현재 todo의 orderNo을 알기
  const deleteTodo = todos.filter((todo) => {
    return todo.todoId === todoId;
  });

  const deleteTodoOrderNo = deleteTodo[0].orderNo;

  // 현재 todo의 orderNo - 1
  todos.forEach((todo) => {
    if (todo.orderNo > deleteTodoOrderNo) {
      updateTodoOrder(todo.todoId, todo.orderNo - 1);
    }
  });
};

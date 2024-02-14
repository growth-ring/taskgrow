import { getTodos } from '../services/todo';
import { updateTodoOrder } from '../services/todo';

import type { Todo } from '../store/todos';
export const todosUpdateOrderNo = async (
  selectedTaskId: number,
  todoId: number,
) => {
  // 해당 task의 모든 todo를 불러옴
  const todos: Todo[] = await getTodos(selectedTaskId);

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

//옮긴 투두의 order 뒤 부터 옮겨진 투두 order까지
export const todosDragUpdateOrderNo = async (
  selectedTaskId: number,
  dragTodoOrderNo: number,
  dragOverTodoOrderNo: number,
) => {
  // 해당 task의 모든 todo를 불러옴
  const todos: Todo[] = await getTodos(selectedTaskId);

  // dragTodo의 orderNo 뒤부터 ~ dragOverTodo의 orderNo 전까지
  if (dragTodoOrderNo < dragOverTodoOrderNo) {
    todos.forEach((todo) => {
      if (
        todo.orderNo > dragTodoOrderNo &&
        todo.orderNo <= dragOverTodoOrderNo
      ) {
        updateTodoOrder(todo.todoId, todo.orderNo - 1);
      }
    });
  } else {
    todos.forEach((todo) => {
      if (
        todo.orderNo < dragTodoOrderNo &&
        todo.orderNo >= dragOverTodoOrderNo
      ) {
        updateTodoOrder(todo.todoId, todo.orderNo + 1);
      }
    });
  }
};

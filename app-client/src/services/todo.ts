import { httpClient } from './api';
import { useLoading } from '../store/loading';

interface AddTodoData {
  taskId: number;
  todo: string;
  orderNo: number;
  performCount: number;
  planCount: number;
}

interface UpdateTodoData {
  todoId: number;
  todo: string;
  status: string;
  planCount: number;
}

const { loadingStart, loadingStop } = useLoading.getState();

export const addTodo = async (todoData: AddTodoData) => {
  loadingStart();
  try {
    const todo = await httpClient.post('/todos', todoData);
    loadingStop();
    return todo.data;
  } catch (error: any) {
    loadingStop();
    alert(error.response.data.message);
  }
};

export const getTodos = async (taskId: number) => {
  loadingStart();
  try {
    const todoData = await httpClient.get('/todos', {
      params: { taskId },
    });
    loadingStop();
    return todoData.data.filter((todo: any) => todo.taskId === taskId);
  } catch (error: any) {
    loadingStop();
    if (error.response.status === 404) {
      return null;
    }
  }
};

export const updatePerformPomodoro = async (todoId: number) => {
  try {
    await httpClient.patch(`/pomodoros/${todoId}/complete`);
  } catch (error: any) {
    return null;
  }
};

const updatePlanPomodoro = async (todoData: UpdateTodoData) => {
  try {
    await httpClient.patch(`/pomodoros/${todoData.todoId}`, {
      planCount: todoData.planCount,
    });
  } catch (error: any) {
    return null;
  }
};

export const updateTodo = async (todoData: UpdateTodoData) => {
  try {
    await httpClient.patch(`/todos/${todoData.todoId}`, {
      todo: todoData.todo,
      status: todoData.status,
    });
    if (todoData.status === 'READY') {
      await updatePlanPomodoro(todoData);
    }
  } catch (error: any) {
    return alert(error.response.data.error);
  }
};

export const deleteTodo = async (todoId: number) => {
  loadingStart();
  try {
    const answer = await httpClient.delete(`/todos/${todoId}`);
    loadingStop();
    return answer.status === 204 ? 'OK' : null;
  } catch (error: any) {
    loadingStop();
    return null;
  }
};

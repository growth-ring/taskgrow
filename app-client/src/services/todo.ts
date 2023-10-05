import axios from 'axios';

interface AddTodoData {
  taskId: number;
  todo: string;
  performCount: number;
  planCount: number;
}

interface UpdateTodoData {
  todoId: number;
  todo: string;
  status: string;
  planCount: number;
}

interface UpdatePomodoro {
  todoId: number;
  performCount: number;
}

export const addTodo = async (todoData: AddTodoData) => {
  try {
    const todo = await axios.post('/httpClient/api/v1/todos', {
      task_id: todoData.taskId,
      todo: todoData.todo,
      plan_count: todoData.planCount,
      perform_count: todoData.performCount,
    });
    return todo.data;
  } catch (error: any) {
    alert(error.response.data.message);
  }
};

export const getTodos = async (taskId: number) => {
  try {
    const todoData = await axios.get('/httpClient/api/v1/todos', {
      params: {
        task_id: taskId,
      },
    });
    return todoData.data.filter((todo: any) => todo.task_id === taskId);
  } catch (error: any) {
    if (error.response.status === 404) {
      return null;
    }
  }
};

export const updatePerformPomodoro = async (pomodoroData: UpdatePomodoro) => {
  try {
    await axios.patch(`/httpClient/api/v1/pomodoros/${pomodoroData.todoId}`, {
      perform_count: pomodoroData.performCount,
    });
  } catch (error: any) {
    return null;
  }
};

const updatePlanPomodoro = async (todoData: UpdateTodoData) => {
  try {
    await axios.patch(`/httpClient/api/v1/pomodoros/${todoData.todoId}`, {
      plan_count: todoData.planCount,
    });
  } catch (error: any) {
    return null;
  }
};

export const updateTodo = async (todoData: UpdateTodoData) => {
  try {
    await axios.patch(`/httpClient/api/v1/todos/${todoData.todoId}`, {
      todo: todoData.todo,
      status: todoData.status,
    });
    if (todoData.status === 'READY') {
      await updatePlanPomodoro(todoData);
    }
  } catch (error: any) {
    return null;
  }
};

export const deleteTodo = async (todoId: number) => {
  try {
    const answer = await axios.delete(`/httpClient/api/v1/todos/${todoId}`);
    return answer.status === 204 ? 'OK' : null;
  } catch (error: any) {
    return null;
  }
};

import axios from 'axios';

interface AddTodoData {
  taskId: number;
  todo: string;
  performCount: number;
  planCount: number;
}

export const addTodo = async (todoData: AddTodoData) => {
  try {
    const todo = await axios.post('/httpClient/api/v1/todos', {
      task_id: todoData.taskId,
      todo: todoData.todo,
      perform_count: todoData.performCount,
      plan_count: todoData.planCount,
    });
    return todo.data;
  } catch (error: any) {
    alert(error.response.data.message);
  }
};

export const getTodo = async (taskId: number) => {
  try {
    const taskData = await axios.get(
      `/httpClient/api/v1/tasks/${taskId}/todos`,
    );
    return taskData.data;
  } catch (error: any) {
    if (error.response.status === 404) {
      return null;
    }
  }
};

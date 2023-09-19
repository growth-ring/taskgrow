import axios from 'axios';

interface AddTodoData {
  taskId: number;
  todo: string;
  performCount: number;
  planCount: number;
}

export const addTodo = async (todoData: AddTodoData) => {
  try {
    await axios.post('/test', {
      task_id: todoData.taskId,
      todo: todoData.todo,
      plan_count: todoData.planCount,
      perform_count: todoData.performCount,
    });
  } catch (error: any) {
    alert(error.response.data.message);
  }
};

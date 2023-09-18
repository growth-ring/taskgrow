import axios from 'axios';

interface AddTaskData {
  userId: number;
  userClickDay: string;
}

interface GetTaskListData {
  userId: number;
  startDate: string;
  endDate: string;
}

export const addTask = async (taskData: AddTaskData) => {
  try {
    const task = await axios.post('/httpClient/api/v1/tasks', {
      user_id: taskData.userId,
      task_date: taskData.userClickDay,
    });
    return task.data.task_id;
  } catch (error: any) {
    alert(error.response.data.message);
  }
};

export const getTask = async (taskId: number) => {
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

export const getTaskList = async (taskData: GetTaskListData) => {
  try {
    const taskListData = await axios.get('/httpClient/api/v1/tasks', {
      params: {
        user_id: taskData.userId,
        start_date: taskData.startDate,
        end_date: taskData.endDate,
      },
    });
    return taskListData.data;
  } catch (error: any) {
    if (error.response.status === 404) {
      return null;
    }
  }
};

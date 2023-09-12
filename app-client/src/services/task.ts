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
    const a = await axios.post('/httpClient/api/v1/tasks', {
      user_id: taskData.userId,
      task_date: taskData.userClickDay,
    });
  } catch (error: any) {
    alert(error.response.data.message);
  }
};

export const getTask = async (taskData: GetTaskData) => {
  try {
    return await axios.get(`/httpClient/api/v1/tasks/${taskData}`);
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
        userId: taskData.userId,
        startDate: taskData.startDate,
        endDate: taskData.endDate,
      },
    });
    return taskListData.data;
  } catch (error: any) {
    if (error.response.status === 404) {
      return null;
    }
  }
};

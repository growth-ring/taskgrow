import { httpClient } from './api';
import { useLoading } from '../store/loading';

interface AddTaskData {
  userId: number;
  userClickDay: string;
}

interface GetTaskListData {
  userId: number;
  startDate: string;
  endDate: string;
}

const { loadingStart, loadingStop } = useLoading.getState();

export const addTask = async (taskData: AddTaskData) => {
  loadingStart();
  try {
    const task = await httpClient.post('/tasks', {
      user_id: taskData.userId,
      task_date: taskData.userClickDay,
    });
    return task.data.task_id;
  } catch (error: any) {
    alert(error.response.data.message);
  }
};

export const getTask = async (taskId: number) => {
  loadingStart();
  try {
    const taskData = await httpClient.get(`/tasks/${taskId}/todos`);
    loadingStop();
    return taskData.data;
  } catch (error: any) {
    loadingStop();
    if (error.response.status === 404) {
      return null;
    }
  }
};

export const getTaskList = async (taskData: GetTaskListData) => {
  loadingStart();
  try {
    const taskListData = await httpClient.get('/tasks', {
      params: {
        user_id: taskData.userId,
        start_date: taskData.startDate,
        end_date: taskData.endDate,
      },
    });
    loadingStop();
    return taskListData.data;
  } catch (error: any) {
    loadingStop();
    if (error.response.status === 404) {
      return null;
    }
  }
};

export const deleteTask = async (taskId: number) => {
  loadingStart();
  try {
    const data = await httpClient.delete(`/tasks/${taskId}`);
    loadingStop();
    return data;
  } catch (error: any) {
    loadingStop();
    return null;
  }
};

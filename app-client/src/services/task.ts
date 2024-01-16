import { httpClient } from './api';
import { useLoading } from '../store/loading';

interface AddTaskData {
  userId: number;
  taskDate: string;
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
    const task = await httpClient.post('/tasks', taskData);
    return task.data.taskId;
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
      params: taskData,
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

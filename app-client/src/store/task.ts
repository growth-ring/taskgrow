import { create } from 'zustand';

interface TaskDate {
  taskId: string;
  taskDate: string;
  todos: {
    remain: number;
    done: number;
  };
}

interface LoginStore {
  monthTaskDate: TaskDate[];
  setMonthTaskDate: (taskDate: TaskDate[]) => void;
  selectedTaskId: number;
  setSelectedTaskId: (selectedTaskId: number) => void;
}

const taskId = localStorage.getItem('taskId');

export const useTask = create<LoginStore>((set) => ({
  monthTaskDate: [],
  setMonthTaskDate: (taskDate) => set({ monthTaskDate: taskDate }),
  selectedTaskId: taskId === null ? 0 : +taskId,
  setSelectedTaskId: (taskId) => set({ selectedTaskId: taskId }),
}));

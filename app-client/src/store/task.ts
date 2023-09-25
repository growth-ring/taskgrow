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

export const useTask = create<LoginStore>((set) => ({
  monthTaskDate: [],
  setMonthTaskDate: (taskDate) => set({ monthTaskDate: taskDate }),
  selectedTaskId: 0,
  setSelectedTaskId: (taskId) => set({ selectedTaskId: taskId }),
}));

import { create } from 'zustand';

interface TaskDateTodo {
  taskId: string;
  taskDate: string;
  todos: {
    remain: number;
    done: number;
  };
  feelingsScore: number;
  todoData: { todo: string; perform_count: number; plan_count: number }[];
}

interface LoginStore {
  monthTaskDate: TaskDateTodo[];
  setMonthTaskDate: (taskDate: TaskDateTodo[]) => void;
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

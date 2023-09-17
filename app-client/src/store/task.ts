import { create } from 'zustand';

interface LoginStore {
  selectedTaskId: number;
  setSelectedTaskId: (selectedTaskId: number) => void;
}

export const useTask = create<LoginStore>((set) => ({
  selectedTaskId: 0,
  setSelectedTaskId: (taskId) => set({ selectedTaskId: taskId }),
}));

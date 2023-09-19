import { create } from 'zustand';

interface TimerStore {
  today: string;
  setToday: (running: string) => void;
  selectedTodo: string;
  setSelectedTodo: (todo: string) => void;
}

export const useTodosStore = create<TimerStore>((set) => ({
  today: '',
  setToday: (day) => set({ today: day }),
  selectedTodo: '오늘 할 일 골라주세요',
  setSelectedTodo: (todo) => set({ selectedTodo: todo }),
}));

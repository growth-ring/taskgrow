import { create } from 'zustand';

interface TimerStore {
    today: string;
    setToday: (running: string) => void;
}

export const useTodosStore = create<TimerStore>((set) => ({
    today: "",
    setToday: (day) => set({ today: day}),
}));
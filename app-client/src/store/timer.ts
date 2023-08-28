import { create } from 'zustand';

interface TimerStore {
    isRunning: boolean;
    setIsRunning: (running: boolean) => void;
    isTimer: boolean;
    setIsTimer: (timer: boolean) => void;
    timerMinute: number;
    setTimerMinute: (minute: number) => void;
}

export const useTimerStore = create<TimerStore>((set) => ({
    isRunning: true,
    setIsRunning: (running) => set({ isRunning: running}),
    isTimer: false,
    setIsTimer: (timer) => set({ isTimer: timer}),
    timerMinute: 1,
    setTimerMinute: (minute) => set({ timerMinute: minute}),
}));
import { create } from 'zustand';

export type TimerState = 'INITIAL' | 'RUNNING' | 'FINISHED' ;

interface TimerStore {
    showTodoBtn: boolean;
    setShowTodoBtn: (btn: boolean) => void;
    onTimer: boolean;
    setOnTimer: (timer: boolean) => void;
    timerState: TimerState;
    startTime: number;
    timerMinute: number;
    start: () => void;
    stop: () => void;
    complete: () => void;
    setTimerMinute: (minute: number) => void;
}

export const useTimerStore = create<TimerStore>((set) => ({
    showTodoBtn: true,
    setShowTodoBtn: (btn) => set({ showTodoBtn : btn}), 
    onTimer: false,
    setOnTimer: (timer) => set({ onTimer: timer}),
    timerState: 'INITIAL',
    startTime: 0,
    start: () => {
        set({
            timerState: 'RUNNING',
            startTime: Date.now(),
        });
    },
    stop: () => {
        set({
            timerState: 'INITIAL',
        });
    },
    complete: () => {
        set({
            timerState: 'FINISHED',
        });
    },
    timerMinute: 1,
    setTimerMinute: (minute) => set({ timerMinute: minute}),
}));

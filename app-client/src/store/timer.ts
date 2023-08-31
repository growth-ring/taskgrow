import { create } from 'zustand';

export type TimerState = 'INITIAL' | 'RUNNING' | 'FINISHED' ;

interface TimerStore {
    timerState: TimerState;
    startTime: number;
    timerMinute: number;
    start: () => void;
    stop: () => void;
    complete: () => void;
    setTimerMinute: (minute: number) => void;
}

export const useTimerStore = create<TimerStore>((set) => ({
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
            timerMinute: 1,
        });
    },
    complete: () => {
        set({
            timerState: 'FINISHED',
            timerMinute: 1,
        });
    },
    timerMinute: 1,
    setTimerMinute: (minute) => set({ timerMinute: minute}),
}));

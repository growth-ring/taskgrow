import { create } from 'zustand';

export type TimerState = 'INITIAL' | 'RUNNING' | 'FINISHED';
export type SelectedBtnState = 'TODO' | 'BREAK' | 'REVIEW';

export interface TimerStore {
  selectedBtn: SelectedBtnState;
  showTodo: () => void;
  showBreak: () => void;
  showReview: () => void;
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
  selectedBtn: 'TODO',
  showTodo: () => set({ selectedBtn: 'TODO' }),
  showBreak: () => set({ selectedBtn: 'BREAK' }),
  showReview: () => set({ selectedBtn: 'REVIEW' }),
  onTimer: false,
  setOnTimer: (timer) => set({ onTimer: timer }),
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
  timerMinute: 25,
  setTimerMinute: (minute) => set({ timerMinute: minute }),
}));

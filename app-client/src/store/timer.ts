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

const userStartTime = Number(localStorage.getItem('startTime'));
const userTodo = localStorage.getItem('todo');

export const useTimerStore = create<TimerStore>((set) => ({
  selectedBtn: userTodo === '휴식' ? 'BREAK' : 'TODO',
  showTodo: () => set({ selectedBtn: 'TODO' }),
  showBreak: () => set({ selectedBtn: 'BREAK' }),
  showReview: () => set({ selectedBtn: 'REVIEW' }),
  onTimer: userStartTime !== 0 ? true : false,
  setOnTimer: (timer) => set({ onTimer: timer }),
  timerState: 'RUNNING',
  startTime: userStartTime,
  start: () => {
    const userStartTime = Date.now();
    set({
      timerState: 'RUNNING',
      startTime: userStartTime,
    });
    localStorage.setItem('startTime', String(userStartTime));
  },
  stop: () => {
    set({
      timerState: 'INITIAL',
    });
    localStorage.setItem('startTime', '0');
  },
  complete: () => {
    set({
      timerState: 'FINISHED',
    });
  },
  timerMinute: userTodo === '휴식' ? 5 : 25,
  setTimerMinute: (minute) => set({ timerMinute: minute }),
}));

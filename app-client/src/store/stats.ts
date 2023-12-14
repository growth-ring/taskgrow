import { create } from 'zustand';

interface DateStore {
  year: number;
  setYear: (year: number) => void;
  month: number;
  setMonth: (month: number) => void;
}

interface StatsStore {
  moodDetail: string;
  setMoodDetail: (moodDetail: string) => void;
  moodTotal: number;
  setMoodTotal: (moodTotal: number) => void;
}

export const currentDate = new Date();

export const useDate = create<DateStore>((set) => ({
  year: currentDate.getFullYear(),
  setYear: (newYear) => set({ year: newYear }),
  month: currentDate.getMonth() + 1,
  setMonth: (newMonth) => set({ month: newMonth }),
}));

export const useStats = create<StatsStore>((set) => ({
  moodDetail: '',
  setMoodDetail: (mood) => set({ moodDetail: mood }),
  moodTotal: 0,
  setMoodTotal: (total) => set({ moodTotal: total }),
}));

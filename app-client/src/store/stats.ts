import { create } from 'zustand';

interface DateStore {
  year: number;
  setYear: (year: number) => void;
  month: number;
  setMonth: (month: number) => void;
}

const currentDate = new Date();

export const useDate = create<DateStore>((set) => ({
  year: currentDate.getFullYear(),
  setYear: (newYear) => set({ year: newYear }),
  month: currentDate.getMonth() + 1,
  setMonth: (newMonth) => set({ month: newMonth }),
}));

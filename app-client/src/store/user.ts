import { create } from 'zustand';

interface UserStore {
  userId: number;
  setUserId: (id: number) => void;
}

const userName = localStorage.getItem('userName');

export const useUser = create<UserStore>((set) => ({
  userId: userName === null ? 0 : +userName,
  setUserId: (id) => set({ userId: id }),
}));

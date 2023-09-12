import { create } from 'zustand';

interface UserStore {
  userId: number;
  setUserId: (id: number) => void;
}

export const useUser = create<UserStore>((set) => ({
  userId: 0,
  setUserId: (id) => set({ userId: id }),
}));

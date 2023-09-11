import { create } from 'zustand';

interface UserStore {
  userId: string;
  setUserId: (id: string) => void;
}

export const useUser = create<UserStore>((set) => ({
  userId: '',
  setUserId: (id) => set({ userId: id }),
}));

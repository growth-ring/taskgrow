import { create } from 'zustand';

interface UserStore {
  userId: number;
  setUserId: (id: number) => void;
}

const userNameId = localStorage.getItem('userNameId');

export const useUser = create<UserStore>((set) => ({
  userId: userNameId === null ? 0 : +userNameId,
  setUserId: (id) => set({ userId: id }),
}));

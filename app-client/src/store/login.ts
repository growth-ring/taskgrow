import { create } from 'zustand';

interface LoginStore {
  isShowLogin: boolean;
  setIsShowLogin: (IsShow: boolean) => void;
}

export const useLogin = create<LoginStore>((set) => ({
  isShowLogin: true,
  setIsShowLogin: (IsShow) => set({ isShowLogin: IsShow }),
}));

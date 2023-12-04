import { create } from 'zustand';

interface LoadingStore {
  loading: boolean;
  loadingStart: () => void;
  loadingStop: () => void;
}

export const useLoading = create<LoadingStore>((set) => ({
  loading: false,
  loadingStart: () => set({ loading: true }),
  loadingStop: () => set({ loading: false }),
}));

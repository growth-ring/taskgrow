import { create } from 'zustand';

export interface ReviewStore {
  isReview: boolean;
  openReview: () => void;
  closeReview: () => void;
}

export const useReviewStore = create<ReviewStore>((set) => ({
  isReview: false,
  openReview: () => set({ isReview: true }),
  closeReview: () => set({ isReview: false }),
}));

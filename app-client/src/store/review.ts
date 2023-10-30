import { create } from 'zustand';

export interface ReviewStore {
  isReview: boolean;
  openReview: () => void;
  closeReview: () => void;
  feelingsScore: number;
  setFeelingsScore: (score: number) => void;
  reviewId: number;
  setReviewId: (id: number) => void;
}

export const useReviewStore = create<ReviewStore>((set) => ({
  isReview: false,
  openReview: () => set({ isReview: true }),
  closeReview: () => set({ isReview: false }),
  feelingsScore: 0,
  setFeelingsScore: (score) => set({ feelingsScore: score }),
  reviewId: 0,
  setReviewId: (id) => set({ reviewId: id }),
}));

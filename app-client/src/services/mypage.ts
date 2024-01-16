import { httpClient } from './api';
import { useLoading } from '../store/loading';

const { loadingStart, loadingStop } = useLoading.getState();

interface Review extends MypageStats {
  feelingsScore: number[];
  page: number;
}

interface Todos extends MypageStats {
  status: string;
  page: number;
}

interface MypageStats {
  userId: string;
  startDate: string;
  endDate: string;
}

export const getReviewDetail = async (userReview: Review) => {
  loadingStart();
  try {
    const review = await httpClient.get(`/mypage/${userReview.userId}/review`, {
      params: {
        page: userReview.page - 1,
        size: 10,
        sort: 'string',
        feelingsScore: userReview.feelingsScore.join(','),
        startDate: userReview.startDate,
        endDate: userReview.endDate,
      },
    });
    loadingStop();
    return review.data.content;
  } catch (error) {
    loadingStop();
    alert('오류가 발생했습니다. 관리자에게 문의해주세요.');
  }
};

export const getReviewStats = async (userReviewStats: MypageStats) => {
  loadingStart();
  try {
    const feelings = await httpClient.get(
      `/mypage/${userReviewStats.userId}/review/stats`,
      {
        params: {
          startDate: userReviewStats.startDate,
          endDate: userReviewStats.endDate,
        },
      },
    );
    loadingStop();
    return feelings.data.feelings;
  } catch (error: any) {
    loadingStop();
    alert('오류가 발생했습니다. 관리자에게 문의해주세요.');
  }
};

export const getTodosStats = async (userTodosStats: MypageStats) => {
  loadingStart();
  try {
    const Todos = await httpClient.get(
      `/mypage/${userTodosStats.userId}/todos/stats`,
      {
        params: {
          startDate: userTodosStats.startDate,
          endDate: userTodosStats.endDate,
        },
      },
    );
    loadingStop();
    return Todos.data;
  } catch (error: any) {
    loadingStop();
    alert('오류가 발생했습니다. 관리자에게 문의해주세요.');
  }
};

export const getTodosDetail = async (userTodos: Todos) => {
  loadingStart();
  try {
    const todos = await httpClient.get(`mypage/${userTodos.userId}/todos`, {
      params: {
        page: userTodos.page - 1,
        size: 10,
        sort: 'string',
        status: userTodos.status,
        startDate: userTodos.startDate,
        endDate: userTodos.endDate,
      },
    });
    loadingStop();
    return todos.data.content;
  } catch {
    loadingStop();
    alert('오류가 발생했습니다. 관리자에게 문의해주세요.');
  }
};

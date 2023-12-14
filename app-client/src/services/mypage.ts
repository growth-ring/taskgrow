import { httpClient } from './api';
import { useLoading } from '../store/loading';

const { loadingStart, loadingStop } = useLoading.getState();

interface Review extends ReviewStats {
  feelingsScore: number[];
}

interface ReviewStats {
  userId: string;
  startDate: string;
  endDate: string;
}

export const getReviewDetail = async (userReview: Review) => {
  loadingStart();
  try {
    const review = await httpClient.get(`/mypage/${userReview.userId}/review`, {
      params: {
        page: 0,
        size: 10,
        sort: 'string',
        feelings_score: userReview.feelingsScore.join(','),
        start_date: userReview.startDate,
        end_date: userReview.endDate,
      },
    });
    loadingStop();
    return review.data.content;
  } catch (error) {
    loadingStop();
    alert('오류가 발생했습니다. 관리자에게 문의해주세요.');
  }
};

export const getReviewStats = async (userReviewStats: ReviewStats) => {
  loadingStart();
  try {
    const feelings = await httpClient.get(
      `/mypage/${userReviewStats.userId}/review/stats`,
      {
        params: {
          start_date: userReviewStats.startDate,
          end_date: userReviewStats.endDate,
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

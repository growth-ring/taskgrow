import { httpClient } from './api';
import { useLoading } from '../store/loading';

interface Review {
  subject: string;
  contents: string;
  feelingsScore: number;
}

interface AddReview extends Review {
  taskId: number;
}

interface UpdateReview extends Review {
  reviewId: number;
}

interface ReviewStats {
  userId: string;
  startDate: string;
  endDate: string;
}

const { loadingStart, loadingStop } = useLoading.getState();

export const addReview = async (reviewData: AddReview) => {
  loadingStart();
  try {
    const review = await httpClient.post('/review', {
      task_id: reviewData.taskId,
      subject: reviewData.subject,
      contents: reviewData.contents,
      feelings_score: reviewData.feelingsScore,
    });
    loadingStop();
    return review.data;
  } catch (error: any) {
    loadingStop();
    const errorMessage = error.response.data.errors[0].reason;

    if (error.response.status === 409) {
      alert('하루에 하나의 회고만 작성할 수 있습니다.');
    } else if (errorMessage) {
      alert(errorMessage);
    } else {
      alert('오류가 발생했습니다. 관리자에게 문의해주세요.');
    }
  }
};

export const getReview = async (taskId: number) => {
  loadingStart();
  try {
    const review = await httpClient.get(`/review/${taskId}`);
    loadingStop();
    return review.data;
  } catch (error: any) {
    loadingStop();
    return null;
  }
};

export const getReviewStats = async (userReviewStats: ReviewStats) => {
  loadingStart();
  try {
    const feelings = await httpClient.get(
      `/review/stats/${userReviewStats.userId}`,
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

export const deleteReview = async (reviewId: number) => {
  loadingStart();
  try {
    const isDelete = await httpClient.delete(`/review/${reviewId}`);
    loadingStop();
    return isDelete;
  } catch (error: any) {
    loadingStop();
    alert('오류가 발생했습니다. 관리자에게 문의해주세요.');
  }
};

export const updateReview = async (reviewData: UpdateReview) => {
  loadingStart();
  try {
    await httpClient.put(`/review/${reviewData.reviewId}`, {
      subject: reviewData.subject,
      contents: reviewData.contents,
      feelings_score: reviewData.feelingsScore,
    });
    loadingStop();
    return alert('회고 수정 되었습니다.');
  } catch (error: any) {
    loadingStop();
    alert('오류가 발생했습니다. 관리자에게 문의해주세요.');
  }
};

import { httpClient } from './api';

interface Review {
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

export const addReview = async (reviewData: AddReview) => {
  try {
    const review = await httpClient.post('/review', {
      task_id: reviewData.taskId,
      contents: reviewData.contents,
      feelings_score: reviewData.feelingsScore,
    });
    if (review.status === 201) {
      return review.data;
    }
  } catch (error: any) {
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
  try {
    const review = await httpClient.get(`/review/${taskId}`);
    return review.data;
  } catch (error: any) {
    return null;
  }
};

export const getReviewStats = async (userReviewStats: ReviewStats) => {
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
    console.log(feelings.data.feelings);
    // return
  } catch (error: any) {
    alert('오류가 발생했습니다. 관리자에게 문의해주세요.');
  }
};

export const deleteReview = async (reviewId: number) => {
  try {
    const isDelete = await httpClient.delete(`/review/${reviewId}`);
    return isDelete.status === 204;
  } catch (error: any) {
    alert('오류가 발생했습니다. 관리자에게 문의해주세요.');
  }
};

export const updateReview = async (reviewData: UpdateReview) => {
  try {
    await httpClient.put(`/review/${reviewData.reviewId}`, {
      contents: reviewData.contents,
      feelings_score: reviewData.feelingsScore,
    });
    return alert('회고 수정 되었습니다.');
  } catch (error: any) {
    alert('오류가 발생했습니다. 관리자에게 문의해주세요.');
  }
};

import axios from 'axios';

interface AddReviewData {
  taskId: number;
  content: string;
  feelingsScore: number;
}

export const addReview = async (reviewData: AddReviewData) => {
  try {
    const review = await axios.post('/httpClient/api/v1/review', {
      task_id: reviewData.taskId,
      content: reviewData.content,
      feelings_score: reviewData.feelingsScore,
    });
    if (review.status === 201) {
      return review.data;
    }
  } catch (error: any) {
    const errorMessage =
      error.response.data.feelingsScore || error.response.data.content;

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
    const review = await axios.get(`/httpClient/api/v1/review/${taskId}`);
    return review.data;
  } catch (error: any) {
    return null;
  }
};

export const deleteReview = async (reviewId: number) => {
  try {
    const isDelete = await axios.delete(
      `/httpClient/api/v1/review/${reviewId}`,
    );
    return isDelete.status === 204;
  } catch (error: any) {
    alert('오류가 발생했습니다. 관리자에게 문의해주세요.');
  }
};

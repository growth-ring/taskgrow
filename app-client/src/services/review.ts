import axios from 'axios';

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

export const addReview = async (reviewData: AddReview) => {
  try {
    const review = await axios.post('/api/v1/review', {
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
    const review = await axios.get(`/api/v1/review/${taskId}`);
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

export const updateReview = async (reviewData: UpdateReview) => {
  try {
    await axios.put(`/api/v1/review/${reviewData.reviewId}`, {
      contents: reviewData.contents,
      feelings_score: reviewData.feelingsScore,
    });
    return alert('회고 수정 되었습니다.');
  } catch (error: any) {
    alert('오류가 발생했습니다. 관리자에게 문의해주세요.');
  }
};

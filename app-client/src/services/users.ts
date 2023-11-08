import { httpClient } from './api';

interface UserFormData {
  name: string;
  password: string;
}

export const signUp = async (
  formData: UserFormData,
  setIsShowLogin: (isShowLogin: boolean) => void,
) => {
  try {
    await httpClient.post('/users', formData);
    setIsShowLogin(true);
    alert('회원가입이 완료되었습니다.');
  } catch (error: any) {
    alert(error.response.data.error);
  }
};

export const login = async (formData: UserFormData) => {
  try {
    const user = await httpClient.post('/login', formData);
    return user.data.user_id;
  } catch (error: any) {
    if (error.response.status === 404 || error.response.status === 401) {
      alert('로그인에 실패했습니다. 이름 또는 비밀번호를 확인하세요.');
    } else {
      alert('오류가 발생했습니다.');
    }
  }
};

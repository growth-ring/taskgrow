import axios from 'axios';

interface UserFormData {
  name: string;
  password: string;
}

export const signUp = async (
  formData: UserFormData,
  setIsShowLogin: (isShowLogin: boolean) => void,
) => {
  try {
    await axios.post('/httpClient/api/v1/users', formData);
    setIsShowLogin(true);
    alert('회원가입이 완료되었습니다.');
  } catch (error) {
    alert(error.response.data.error);
  }
};

export const login = async (formData: UserFormData) => {
  const name = formData.name;
  try {
    await axios.get(`/httpClient/api/v1/users/${name}`);
  } catch (error) {
    if (error.response.status === 404) {
      alert('아이디 또는 비밀번호가 일치하지 않습니다. 다시 입력해 주세요.');
    } else {
      alert('오류가 발생했습니다.');
    }
  }
};

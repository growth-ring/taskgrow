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
    alert(error.response.data.message);
  }
};

export const login = async (formData: UserFormData) => {
  const name = formData.name;
  try {
    return await axios.get(`/httpClient/api/v1/users/${name}`);
  } catch (error) {
    if (error.response.status === 404) {
      alert('오류입니다');
    }
  }
};

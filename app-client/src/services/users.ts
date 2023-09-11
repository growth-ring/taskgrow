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
  } catch (error) {
    alert(error.response.data.message);
  }
};

export const login = async (formData: UserFormData) => {
  const name = formData.name;
  try {
    const data = await axios.get(`/httpClient/api/v1/users/${name}`);
    console.log('data: ', data.data);
  } catch (error) {
    if (error.response && error.response.status === 404) {
      alert('오류입니다');
    }
  }
};

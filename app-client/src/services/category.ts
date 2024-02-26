import { httpClient } from './api';

export const getCategory = async () => {
  try {
    const categories = await httpClient.get('/categories');
    return categories.data;
  } catch (error: any) {
    alert(error.response.data.message);
  }
};

export const addCategory = async (category: { name: string }) => {
  try {
    await httpClient.post('/categories', category);
  } catch (error: any) {
    alert(error.response.data.message);
  }
};

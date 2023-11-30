import axios from 'axios';

const BASE_URL = '/httpClient/api/v1';
// const BASE_URL = '/api/v1';

export const httpClient = axios.create({
  baseURL: BASE_URL,
});

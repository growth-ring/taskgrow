import { BrowserRouter, Route, Routes } from 'react-router-dom';

import MainPage from '../pages/MainPage';
import TaskPage from '../pages/TaskPage';
import Todos from '../pages/TodosPage';

const Router = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<MainPage />} />
        <Route path="/tasks" element={<TaskPage />} />
        <Route path="/todos/:date" element={<Todos />} />
      </Routes>
    </BrowserRouter>
  );
};

export default Router;

import { BrowserRouter, Route, Routes } from 'react-router-dom';

import MainPage from '../pages/MainPage';
import Todos from '../pages/Todos';

const Router = () => {
    return (
    <BrowserRouter>
    <Routes>
        <Route path="/" element={<MainPage />} />
        <Route path="/todos" element={<Todos />} />
    </Routes>
    </BrowserRouter>
    );
};

export default Router;

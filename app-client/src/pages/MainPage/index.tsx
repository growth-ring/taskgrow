import { useState } from 'react';

import Login from '../../components/User/Login/Login';
import Join from '../../components/User/Join/Join';

const MainPage = () => {
  const [isShow, setIsShow] = useState<boolean>(true);

  const getIsShow = () => {
    setIsShow(!isShow);
  };

  return (
    <div
      className="flex h-screen w-full items-center justify-center bg-gray-900 bg-no-repeat"
      style={{
        backgroundColor: 'var(--main-color)',
      }}
    >
      <div className="rounded-xl bg-white bg-opacity-50 px-16 pt-10 shadow-lg backdrop-blur-md max-sm:px-8">
        {isShow && <Login getIsShow={getIsShow} />}
        {!isShow && <Join getIsShow={getIsShow} />}
      </div>
    </div>
  );
};

export default MainPage;

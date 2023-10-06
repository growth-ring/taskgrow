import { useLogin } from '../../store/login';

import Login from '../../components/User/Login/Login';
import SignUp from '../../components/User/SignUp/SignUp';

const MainPage = () => {
  const { isShowLogin } = useLogin();

  return (
    <div
      className="flex h-full w-full items-center justify-center"
      style={{
        backgroundColor: 'var(--main-color)',
      }}
    >
      <div className="rounded-xl bg-white bg-opacity-50 px-16 pt-10 shadow-lg backdrop-blur-md max-sm:px-8">
        {isShowLogin && <Login />}
        {!isShowLogin && <SignUp />}
      </div>
    </div>
  );
};

export default MainPage;

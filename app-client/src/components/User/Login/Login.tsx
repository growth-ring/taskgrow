import { useState } from 'react';
import { useLogin } from '../../../store/login';
import { login } from '../../../services/users';
import logo from '../../../assets/logo.png';

const Login = () => {
  const { setIsShowLogin } = useLogin();
  const [name, setName] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = (event: React.FormEvent) => {
    event.preventDefault();

    const form = { name, password };
    login(form);
  };

  return (
    <>
      <div>
        <div className="mb-8 flex flex-col items-center text-white">
          <img src={logo} width={150} alt="로고" />
          <h1 className="mb-2 text-2xl">Task grow</h1>
          <span className="text-gray-300">Pomodoro & Todo App</span>
        </div>
        <form onSubmit={handleSubmit}>
          <div className="mb-4 text-lg">
            <input
              type="text"
              name="name"
              placeholder="이름"
              onChange={(e) => setName(e.target.value)}
              className={
                'rounded-3xl border-none bg-yellow-400 bg-opacity-50 px-6 py-2 text-center text-inherit placeholder-slate-200 shadow-lg outline-none backdrop-blur-md'
              }
            />
          </div>

          <div className="mb-4 text-lg">
            <input
              type="text"
              name="password"
              placeholder="비밀번호"
              onChange={(e) => setPassword(e.target.value)}
              className={
                'rounded-3xl border-none bg-yellow-400 bg-opacity-50 px-6 py-2 text-center text-inherit placeholder-slate-200 shadow-lg outline-none backdrop-blur-md'
              }
            />
          </div>
          <div className="flex justify-center text-lg text-black">
            <button
              type="submit"
              style={{ marginTop: '20px' }}
              className="rounded-3xl bg-opacity-80 px-10 py-2 shadow-xl backdrop-blur-md transition-colors duration-300 text-gray hover:bg-main-color hover:text-white"
            >
              로그인
            </button>
          </div>
        </form>
      </div>
      <div
        style={{
          display: 'flex',
          justifyContent: 'flex-end',
          paddingTop: '1.5rem',
          paddingBottom: '1rem',
        }}
      >
        <button
          onClick={() => setIsShowLogin(false)}
          className="text-gray hover:underline"
        >
          회원가입
        </button>
      </div>
    </>
  );
};

export default Login;

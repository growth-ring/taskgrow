import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import { login } from '../../services/users';
import { signUp } from '../../services/users';
import { useUser } from '../../store/user';
import { useLogin } from '../../store/login';

type Form = {
  name: string;
  password: string;
};

const Form = ({ type }: { type: string }) => {
  const navigate = useNavigate();
  const { setUserId } = useUser();
  const { setIsShowLogin } = useLogin();
  const [name, setName] = useState('');
  const [password, setPassword] = useState('');

  const moveLogin = async (form: Form) => {
    const user = await login(form);

    if (user) {
      navigate(`/tasks`);
      setUserId(user);
      localStorage.setItem('userName', name);
      localStorage.setItem('userNameId', user);
    }
  };

  const moveSingUp = (form: Form) => {
    signUp(form, setIsShowLogin);
  };

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    const form = { name, password };
    type === 'login' ? moveLogin(form) : moveSingUp(form);
  };

  return (
    <form onSubmit={handleSubmit}>
      <div className="mb-4 text-lg">
        <input
          type="text"
          name="name"
          placeholder="이름"
          required
          onChange={(e) => setName(e.target.value)}
          className={
            'rounded-3xl border-none bg-yellow-400 bg-opacity-50 px-6 py-2 text-center text-inherit placeholder-slate-200 shadow-lg outline-none backdrop-blur-md'
          }
        />
      </div>

      <div className="mb-4 text-lg">
        <input
          type="password"
          name="password"
          placeholder="비밀번호"
          required
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
          {type === 'login' ? '로그인' : '가입하기'}
        </button>
      </div>
    </form>
  );
};

export default Form;

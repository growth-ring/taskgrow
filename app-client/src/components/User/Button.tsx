import { useLogin } from '../../store/login';

const Button = ({ type }: { type: string }) => {
  const { setIsShowLogin } = useLogin();

  return (
    <div
      style={{
        display: 'flex',
        justifyContent: 'flex-end',
        paddingTop: '1.5rem',
        paddingBottom: '1rem',
      }}
    >
      <button
        onClick={() =>
          type === 'login' ? setIsShowLogin(false) : setIsShowLogin(true)
        }
        className="text-gray hover:underline"
      >
        {type === 'login' ? '회원가입' : '돌아가기'}
      </button>
    </div>
  );
};

export default Button;

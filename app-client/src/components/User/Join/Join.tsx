import logo from '../../../assets/logo.png';

interface LoginProps {
  getIsShow: () => void;
}

const Join = ({ getIsShow }: LoginProps) => {
  return (
    <>
      <div>
        <div className="mb-8 flex flex-col items-center text-white">
          <img src={logo} width={150} alt="로고" />
          <h1 className="mb-2 text-2xl">회원가입</h1>
          <span className="text-gray-300">이름과 비밀번호를 입력해 주세요</span>
        </div>
        <form action="#">
          <div className="mb-4 text-lg">
            <input
              type="text"
              name="name"
              placeholder="이름"
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
              가입하기
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
        <button onClick={getIsShow} className="text-gray hover:underline">
          돌아가기
        </button>
      </div>
    </>
  );
};

export default Join;

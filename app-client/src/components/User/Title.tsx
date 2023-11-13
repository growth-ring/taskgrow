import logo from '../../assets/logo.png';

type Title = {
  title: string;
  subTitle: string;
};

const Title = ({ title, subTitle }: Title) => {
  return (
    <>
      <div className="mb-8 flex flex-col items-center text-white">
        <img src={logo} width={150} alt="로고" />
        <h1 className="mb-2 text-2xl">{title}</h1>
        <span className="text-gray-300">{subTitle}</span>
      </div>
    </>
  );
};

export default Title;

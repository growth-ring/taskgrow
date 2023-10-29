import styled from 'styled-components';

const MainTitle = styled.div`
  @media (max-width: 767px) {
    font-size: 20px;
  }

  @media (min-width: 768px) and (max-width: 1023px) {
    font-size: 25px;
  }

  @media (min-width: 1024px) {
    font-size: 30px;
  }
`;

const SubTitle = styled.div`
  color: gray;

  @media (max-width: 767px) {
    margin-top: 5px;
    font-size: 12px;
  }

  @media (min-width: 768px) and (max-width: 1023px) {
    margin-top: 10px;
    font-size: 15px;
  }

  @media (min-width: 1024px) {
    margin-top: 10px;
    font-size: 18px;
  }
`;

const Title = () => {
  return (
    <>
      <MainTitle>오늘 하루 어땠나요?</MainTitle>
      <SubTitle>기분을 1 ~ 10까지 표현해주세요</SubTitle>
    </>
  );
};

export default Title;

import styled from 'styled-components';
import { SlCalender } from 'react-icons/sl';
import { useNavigate } from 'react-router-dom';

const Container = styled.div`
  display: flex;
  justify-content: space-between;

  @media (max-width: 767px) {
    padding: 0 20px;
    height: 50px;
  }

  @media (min-width: 768px) {
    padding: 0 40px;
    height: 64px;
  }
`;

const Title = styled.div`
  display: flex;
  align-items: center;

  @media (max-width: 767px) {
    font-size: 18px;
    margin: 0 20px;
  }

  @media (min-width: 768px) and (max-width: 1023px) {
    font-size: 20px;
    margin: 0 60px;
  }

  @media (min-width: 1024px) {
    font-size: 22px;
    margin: 0 100px;
  }
`;

const GoBack = styled.button`
  @media (max-width: 767px) {
    margin-right: 10px;
  }

  @media (min-width: 768px) and (max-width: 1023px) {
    margin-right: 30px;
  }

  @media (min-width: 1024px) {
    margin-right: 40px;
  }
`;

const Wrapper = styled.div`
  display: flex;
  align-items: center;

  @media (max-width: 767px) {
    font-size: 21px;
    margin: 0 20px;
  }

  @media (min-width: 768px) and (max-width: 1023px) {
    font-size: 23px;
    margin: 0 60px;
  }

  @media (min-width: 1024px) {
    font-size: 25px;
    margin: 0 100px;
  }
`;

const Header = ({ title }: { title: string }) => {
  const navigate = useNavigate();
  const userName = localStorage.getItem('userName');

  const handleGoBack = () => {
    navigate('/tasks');
  };

  return (
    <>
      <Container>
        <Title>
          🚀 {userName}님의 {title}
        </Title>
        <Wrapper>
          <GoBack onClick={handleGoBack}>
            <SlCalender />
          </GoBack>
        </Wrapper>
      </Container>
    </>
  );
};

export default Header;

import styled from 'styled-components';
import Header from '../../components/Menu/Header';
import HeaderDate from '../../components/Stats/HeaderDate';

const Line = styled.div`
  background-color: #f5f5f5;
`;

const Container = styled.div`
  @media (max-width: 767px) {
    padding-bottom: 20px;
    margin-top: 20px;
  }

  @media (min-width: 768px) {
    display: flex;
    width: 100%;
    height: 90%;
  }
`;

const MyPage = () => {
  return (
    <>
      <Header title="통계" />
      <Line style={{ height: '2px' }} />
      <HeaderDate />
      <Container></Container>
    </>
  );
};

export default MyPage;

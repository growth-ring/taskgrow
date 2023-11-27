import styled from 'styled-components';
import Header from '../../components/Menu/Header';
import HeaderDate from '../../components/Stats/HeaderDate';
import TodosStats from '../../components/Stats/Todos/TodosStats';

const Line = styled.div`
  background-color: #f5f5f5;
`;

const Container = styled.div`
  width: 100%;
  margin-top: 50px;

  @media (max-width: 767px) {
    padding-bottom: 20px;
    margin-top: 20px;
  }

  @media (min-width: 768px) {
    display: flex;
  }
`;

const MyPage = () => {
  return (
    <>
      <Header title="통계" />
      <Line style={{ height: '2px' }} />
      <HeaderDate />
      <Container>
        <TodosStats />
      </Container>
    </>
  );
};

export default MyPage;

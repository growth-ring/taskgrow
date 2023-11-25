import styled from 'styled-components';
import Header from '../../components/Menu/Header';

const Line = styled.div`
  background-color: #f5f5f5;
`;

const MyPage = () => {
  return (
    <>
      <Header title="통계" />
      <Line style={{ height: '2px' }} />
    </>
  );
};

export default MyPage;

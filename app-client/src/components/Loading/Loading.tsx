import styled from 'styled-components';
import spinner from '../../assets/spinner.gif';

export const Container = styled.div`
  position: absolute;
  width: 100vw;
  height: 100vh;
  top: 0;
  left: 0;
  z-index: 999;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
`;

export const LoadingText = styled.div`
  text-align: center;
`;

const Loading = () => {
  return (
    <Container>
      <LoadingText>잠시만 기다려 주세요</LoadingText>
      <img src={spinner} alt="로딩" width="5%" />
    </Container>
  );
};

export default Loading;

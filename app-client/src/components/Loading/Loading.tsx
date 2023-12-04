import styled from 'styled-components';
import spinner from '../../assets/spinner.gif';
import { useLoading } from '../../store/loading';

export const Container = styled.div`
  position: absolute;
  width: 100%;
  height: 100%;
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
  const { loading } = useLoading();

  return (
    <>
      {loading && (
        <Container>
          <LoadingText>잠시만 기다려 주세요</LoadingText>
          <img src={spinner} alt="로딩" width="5%" />
        </Container>
      )}
    </>
  );
};

export default Loading;

import styled from 'styled-components';
import Circle from '../Todos/Circle';

const Container = styled.div`
  width: 75%;

  @media (max-width: 767px) {
    margin: 0 10px;
  }

  @media (min-width: 768px) and (max-width: 1023px) {
    margin: 0 100px;
  }

  @media (min-width: 1024px) {
    margin: 0 150px;
  }
`;

const Content = styled.div`
  margin-top: 10px;

  background-color: white;
  padding: 1rem;
  border-radius: 1rem;
  box-shadow:
    3px 3px 10px rgba(50, 50, 50, 0.1),
    3px 3px 10px rgba(50, 50, 50, 0.1);
`;

const Title = styled.div`
  display: flex;
  justify-content: space-between;
  margin: 0 10px;
  font-size: 18px;
`;

const Text = styled.div`
  margin-top: 10px;
  @media (max-width: 767px) {
    font-size: 18px;
  }

  @media (min-width: 768px) and (max-width: 1023px) {
    font-size: 20px;
  }

  @media (min-width: 1024px) {
    font-size: 22px;
  }
`;

const SubText = styled.div`
  margin-top: 10px;
  color: #949494;
`;

const Box = () => {
  return (
    <Container>
      <Title>
        <div>한 일</div>
        <div>총 30개</div>
      </Title>
      <Content>
        <Circle />
        <Text>완료 달성률은 80% 이에요</Text>
        <SubText>잘하고 있어요! 🥳</SubText>
      </Content>
    </Container>
  );
};

export default Box;

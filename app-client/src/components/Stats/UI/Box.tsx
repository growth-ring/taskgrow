import styled from 'styled-components';
import Circle from '../Todos/Circle';

const Container = styled.div<{ category: string }>`
  width: 75%;

  @media (max-width: 767px) {
    ${({ category }) => category === '한 일' && `margin: 0 10px;`}
  }

  @media (min-width: 768px) and (max-width: 1023px) {
    ${({ category }) => category === '한 일' && `margin: 0 100px;`}
  }

  @media (min-width: 1024px) {
    ${({ category }) => category === '한 일' && `margin: 0 150px;`}
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

const TitleBox = styled.div`
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

interface BoxType {
  title: string[];
  comment: string;
  subComment: string;
}

const Box = ({ title, comment, subComment }: BoxType) => {
  const category = title[0];
  const count = title[1];
  return (
    <Container category={category}>
      <TitleBox>
        <div>{category}</div>
        <div>총 {count}개</div>
      </TitleBox>
      <Content>
        {category === '한 일' && <Circle />}
        <Text>{comment}</Text>
        <SubText>{subComment}</SubText>
      </Content>
    </Container>
  );
};

export default Box;

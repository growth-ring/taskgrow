import styled from 'styled-components';

export interface CircleType {
  getIsDetail: (params: { action: boolean; category?: string }) => void;
}

const Container = styled.div`
  width: 30rem;

  @media (max-width: 767px) {
    font-size: 16px;
  }

  @media (min-width: 768px) and (max-width: 1023px) {
    font-size: 14px;
  }

  @media (min-width: 1024px) {
    font-size: 16px;
  }
`;

const TitleBox = styled.div`
  display: flex;
  height: 20px;
  justify-content: space-between;
  align-items: center;
  margin: 6px 0;
  color: #949494;
`;

const Line = styled.div`
  background-color: #f5f5f5;
`;

const Content = styled.div`
  height: 220px;
`;

const Text = styled.div`
  display: flex;
  justify-content: space-between;
  margin-top: 6.5px;
`;

const Detail = () => {
  return (
    <>
      <Container>
        <TitleBox>
          <div>제목</div>
          <div>뽀모도로 개수</div>
        </TitleBox>
        <Line style={{ height: '2px' }} />
        <Content>
          <Text>
            <div>책읽기</div>
            <div>0 / 3</div>
          </Text>
          <Text>
            <div>책읽기</div>
            <div>0 / 3</div>
          </Text>
          <Text>
            <div>책읽기</div>
            <div>0 / 3</div>
          </Text>
          <Text>
            <div>책읽기</div>
            <div>0 / 3</div>
          </Text>
          <Text>
            <div>책읽기</div>
            <div>0 / 3</div>
          </Text>
          <Text>
            <div>책읽기</div>
            <div>0 / 3</div>
          </Text>
          <Text>
            <div>책읽기</div>
            <div>0 / 3</div>
          </Text>
          <Text>
            <div>책읽기</div>
            <div>0 / 3</div>
          </Text>
          <Text>
            <div>책읽기</div>
            <div>0 / 3</div>
          </Text>
          <Text>
            <div>책읽기</div>
            <div>0 / 3</div>
          </Text>
        </Content>
      </Container>
    </>
  );
};

export default Detail;

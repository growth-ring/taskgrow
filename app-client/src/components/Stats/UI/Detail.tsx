import styled from 'styled-components';
import { useMoods } from '../../../store/mood';

export interface CircleType {
  getIsDetail: (params: {
    action: boolean;
    category?: string;
    subject?: string;
  }) => void;
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

const Detail = ({ category }: { category: string }) => {
  const { moodDetail } = useMoods();
  const isMoon = category.includes('감정');

  return (
    <>
      <Container>
        <TitleBox>
          <div>제목</div>
          <div>{isMoon ? '날짜' : '뽀모도로 개수'}</div>
        </TitleBox>
        <Line style={{ height: '2px' }} />
        <Content>
          {moodDetail.map((mood) => (
            <Text key={mood.review_id}>
              <div>{mood.subject}</div>
              <div>{mood.task_date}</div>
            </Text>
          ))}
        </Content>
      </Container>
    </>
  );
};

export default Detail;

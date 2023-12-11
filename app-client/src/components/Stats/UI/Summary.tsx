import styled from 'styled-components';
import Circle from '../Todos/Circle';
import Chart from '../Mood/Chart';

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

interface SummaryType {
  getIsDetail: (params: { action: boolean; category?: string }) => void;
  category: string;
  comment: string;
  subComment: string;
}

const Summary = ({
  getIsDetail,
  category,
  comment,
  subComment,
}: SummaryType) => {
  return (
    <>
      {category === '한 일' && <Circle getIsDetail={getIsDetail} />}
      {category === '감정' && <Chart getIsDetail={getIsDetail} />}
      <Text>{comment}</Text>
      <SubText>{subComment}</SubText>
    </>
  );
};

export default Summary;

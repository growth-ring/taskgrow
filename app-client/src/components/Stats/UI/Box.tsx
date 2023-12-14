import { useState } from 'react';
import styled from 'styled-components';
import Summary from './Summary';
import Detail from './Detail';
import NavigationButtons from './NavigationButtons';

const Content = styled.div<{ isDetail: boolean }>`
  display: flex;
  flex-direction: ${(props) => (props.isDetail ? 'row' : 'column')};
  justify-content: center;
  text-align: center;
  margin-top: 10px;

  background-color: white;
  padding: ${(props) => (props.isDetail ? '0 1rem' : '1rem')};
  border-radius: 1rem;
  box-shadow:
    3px 3px 10px rgba(50, 50, 50, 0.1),
    3px 3px 10px rgba(50, 50, 50, 0.1);

  height: 270px;
`;

const TitleBox = styled.div`
  display: flex;
  justify-content: space-between;
  margin: 0 10px;
  font-size: 18px;
`;

interface BoxType {
  title: string[];
  comment: string;
  subComment: string;
}

const Box = ({ title, comment, subComment }: BoxType) => {
  const [isDetail, setIsDetail] = useState(false);
  const [category, setCategory] = useState(title[0]);
  const count = title[1];

  const getIsDetail = ({
    action,
    category,
  }: {
    action: boolean;
    category?: string;
  }) => {
    setIsDetail(action);
    if (category !== undefined) {
      setCategory(category);
    }
  };

  return (
    <>
      <TitleBox>
        <div>{category}</div>
        {isDetail && (
          <NavigationButtons category={title[0]} getIsDetail={getIsDetail} />
        )}
        {!isDetail && <div>총 {count}개</div>}
      </TitleBox>
      <Content isDetail={isDetail}>
        {isDetail && <Detail category={category} />}
        {!isDetail && (
          <Summary
            getIsDetail={getIsDetail}
            category={category}
            comment={comment}
            subComment={subComment}
          />
        )}
      </Content>
    </>
  );
};

export default Box;

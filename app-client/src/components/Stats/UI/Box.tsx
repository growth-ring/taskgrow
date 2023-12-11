import { useState } from 'react';
import styled from 'styled-components';
import Summary from './Summary';
import Detail from './Detail';

const Content = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  text-align: center;
  margin-top: 10px;

  background-color: white;
  padding: 1rem;
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
  const category = title[0];
  const count = title[1];

  const getIsDetail = (action: boolean) => {
    setIsDetail(action);
  };

  return (
    <>
      <TitleBox>
        <div>{category}</div>
        <div>총 {count}개</div>
      </TitleBox>
      <Content>
        {isDetail && <Detail getIsDetail={getIsDetail} />}
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

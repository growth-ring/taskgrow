import { useState } from 'react';
import styled from 'styled-components';

const Category = () => {
  const [isAddOption, SetIsAddOption] = useState(false);

  return (
    <List>
      <Option>
        <span>카테고리</span>
        <button onClick={() => SetIsAddOption(!isAddOption)}>+</button>
      </Option>
      {isAddOption && <AddOption placeholder="새 카테고리 입력하세요" />}
      <Ul>
        <Li>카테고리 예시</Li>
      </Ul>
    </List>
  );
};

export default Category;

const Ul = styled.ul`
  & > li {
    margin-bottom: 10px;
  }

  & > li:first-of-type {
    margin-top: 10px;
  }

  cursor: pointer;
  list-style-type: none;
  display: flex;
  flex-direction: column;
  align-items: center;
`;

const Li = styled.li`
  padding: 5px;
  border-radius: 5px;
`;

const Option = styled.div`
  display: flex;
  justify-content: space-between;
  padding: 5px 10px 0 10px;
  color: gray;
  font-size: 15px;
`;

const List = styled.div`
  background: white;
  position: absolute;
  top: 30px;
  width: 180px;
  max-height: 300px;
  overflow-y: auto;
  box-shadow: 5px 5px 10px rgba(0, 0, 0, 0.2);
  border-radius: 3px;
`;

const AddOption = styled.input`
  width: 100%;
  margin-top: 5px;
  border: 1px solid gray;
  border-radius: 5px;
  padding: 3px;
`;

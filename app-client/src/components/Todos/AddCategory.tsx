import { useState } from 'react';
import styled from 'styled-components';
import Category from './Category';

interface AddCategroyProps {
  handleTodoCategoryChange: (categoryId: number) => void;
}

const AddCategroy = ({ handleTodoCategoryChange }: AddCategroyProps) => {
  const [view, setView] = useState(false);

  return (
    <Container>
      <CategoryButton onClick={() => setView(!view)}>
        카테고리
        {view ? ' ▲' : ' ▼'}
      </CategoryButton>
      {view && <Category handleTodoCategoryChange={handleTodoCategoryChange} />}
    </Container>
  );
};

export default AddCategroy;

const Container = styled.div`
  position: relative;
`;

const CategoryButton = styled.button`
  width: 90px;
`;

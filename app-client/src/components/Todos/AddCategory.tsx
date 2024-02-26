import { useEffect, useState } from 'react';
import styled from 'styled-components';
import Category from './Category';
import { useTodosStore } from '../../store/todos';

interface AddCategroyProps {
  handleTodoCategoryChange: (categoryId: number | null) => void;
}

const AddCategroy = ({ handleTodoCategoryChange }: AddCategroyProps) => {
  const { isTodoChange } = useTodosStore();
  const [view, setView] = useState(false);
  const [choiceCategory, setChoiceCategory] = useState('카테고리');

  const handleChoiceCategoryChange = (category: string) => {
    setChoiceCategory(category);
  };

  useEffect(() => {
    handleTodoCategoryChange(null);
    setChoiceCategory('카테고리');
  }, [isTodoChange]);

  return (
    <Container>
      <CategoryButton onClick={() => setView(!view)}>
        {choiceCategory}
        {view ? ' ▲' : ' ▼'}
      </CategoryButton>
      {view && (
        <Category
          close={() => setView(!view)}
          handleTodoCategoryChange={handleTodoCategoryChange}
          handleChoiceCategoryChange={handleChoiceCategoryChange}
        />
      )}
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

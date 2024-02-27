import { useEffect, useState } from 'react';
import styled from 'styled-components';
import Category from './Category';
import { useTodosStore } from '../../store/todos';

interface AddCategroyProps {
  handleTodoCategoryChange: (category: number | null | string) => void;
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
        <Text>{choiceCategory}</Text>
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
  display: flex;
  align-items: center;
  width: 80px;
`;

const Text = styled.span`
  display: inline-block;
  width: 70px;
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: auto;
`;

import { useState, useEffect } from 'react';
import styled from 'styled-components';
import { addCategory, getCategory } from '../../services/category';

interface AddCategroyProps {
  close: () => void;
  handleTodoCategoryChange: (categoryId: number) => void;
  handleChoiceCategoryChange: (category: string) => void;
}

interface CategoriesType {
  id: number;
  name: string;
}

const Category = ({
  close,
  handleTodoCategoryChange,
  handleChoiceCategoryChange,
}: AddCategroyProps) => {
  const [categories, setCategories] = useState<CategoriesType[]>();
  const [category, setCategory] = useState('');
  const [isAddOption, setIsAddOption] = useState(false);

  const handleOptionChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setCategory(e.target.value);
  };

  const handleAddOption = async (e: React.SyntheticEvent) => {
    e.preventDefault();
    const newCategory = { name: category };
    await addCategory(newCategory);
    const newCategories = await getCategory();
    setCategories(newCategories);
    setCategory('');
  };

  const handleClickOption = (category: CategoriesType) => {
    handleTodoCategoryChange(category.id);
    handleChoiceCategoryChange(category.name);
    close();
  };

  useEffect(() => {
    const getCategories = async () => {
      setCategories(await getCategory());
    };
    getCategories();
  }, []);

  return (
    <List>
      <Option>
        <span>카테고리</span>
        <button onClick={() => setIsAddOption(!isAddOption)}>+</button>
      </Option>
      {isAddOption && (
        <AddOption onSubmit={(e) => handleAddOption(e)}>
          <Input
            value={category}
            onChange={handleOptionChange}
            placeholder="새 카테고리 입력하세요"
          />
        </AddOption>
      )}
      {categories && (
        <Ul>
          {categories.map((category, index) => (
            <Li key={index} onClick={() => handleClickOption(category)}>
              {category.name}
            </Li>
          ))}
        </Ul>
      )}
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

const AddOption = styled.form``;

const Input = styled.input`
  width: 100%;
  margin-top: 5px;
  border: 1px solid gray;
  border-radius: 5px;
  padding: 3px;
`;

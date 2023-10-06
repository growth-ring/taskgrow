import { useState } from 'react';
import styled from 'styled-components';
import { SlPlus } from 'react-icons/sl';
import AddTodos from './AddTodos';
import HeaderDate from './HeaderDate';

const Container = styled.div`
  display: flex;
  justify-content: space-between;

  @media (max-width: 767px) {
    padding: 0 20px;
    height: 50px;
  }

  @media (min-width: 768px) {
    padding: 0 40px;
    height: 64px;
  }
`;

const Date = styled.div`
  display: flex;
  align-items: center;

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

const AddTodo = styled.button`
  @media (max-width: 767px) {
    font-size: 21px;
    margin-right: 20px;
  }

  @media (min-width: 768px) and (max-width: 1023px) {
    font-size: 23px;
    margin-right: 50px;
  }

  @media (min-width: 1024px) {
    font-size: 25px;
    margin-right: 100px;
  }
`;

const Header = () => {
  const [showAddTodos, setShowAddTodos] = useState(false);

  const handleShowAddTodos = () => {
    setShowAddTodos(true);
  };

  const getShowAddTodos = (todos: boolean) => {
    setShowAddTodos(todos);
  };

  return (
    <>
      <Container>
        <Date>
          <HeaderDate />
        </Date>
        <AddTodo onClick={handleShowAddTodos}>
          <SlPlus />
        </AddTodo>
      </Container>
      {showAddTodos && <AddTodos getShowAddTodos={getShowAddTodos} />}
    </>
  );
};

export default Header;

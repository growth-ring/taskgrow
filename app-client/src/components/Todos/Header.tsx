import { useState } from 'react';
import styled from 'styled-components';
import { SlPlus, SlCalender } from 'react-icons/sl';
import AddTodos from './AddTodos';
import HeaderDate from './HeaderDate';
import Alert from '../UI/Alert';
import { useNavigate } from 'react-router-dom';
import { useTimerStore } from '../../store/timer';

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
    margin-right: 20px;
  }

  @media (min-width: 768px) and (max-width: 1023px) {
    margin-right: 50px;
  }

  @media (min-width: 1024px) {
    margin-right: 100px;
  }
`;

const GoBack = styled.button`
  @media (max-width: 767px) {
    margin-right: 10px;
  }

  @media (min-width: 768px) and (max-width: 1023px) {
    margin-right: 30px;
  }

  @media (min-width: 1024px) {
    margin-right: 40px;
  }
`;

const Wrapper = styled.div`
  display: flex;
  align-items: center;

  @media (max-width: 767px) {
    font-size: 21px;
  }

  @media (min-width: 768px) and (max-width: 1023px) {
    font-size: 23px;
  }

  @media (min-width: 1024px) {
    font-size: 25px;
  }
`;

const Header = () => {
  const navigate = useNavigate();
  const { timerState } = useTimerStore();
  const [showAddTodos, setShowAddTodos] = useState(false);
  const [showGoBack, setShowGoBack] = useState(false);

  const handleShowAddTodos = () => {
    setShowAddTodos(true);
  };

  const getShowAddTodos = (todos: boolean) => {
    setShowAddTodos(todos);
  };

  const handleGoBack = () => {
    if (timerState === 'RUNNING') {
      setShowGoBack(true);
    } else {
      navigate('/tasks');
    }
  };

  const getIsShow = () => {
    setShowGoBack(!showGoBack);
  };

  return (
    <>
      <Container>
        <Date>
          <HeaderDate />
        </Date>
        <Wrapper>
          <GoBack onClick={handleGoBack}>
            <SlCalender />
          </GoBack>
          <AddTodo onClick={handleShowAddTodos}>
            <SlPlus />
          </AddTodo>
        </Wrapper>
      </Container>
      {showAddTodos && <AddTodos getShowAddTodos={getShowAddTodos} />}
      {showGoBack && <Alert text={'task'} getIsShow={getIsShow} />}
    </>
  );
};

export default Header;

import { useState } from 'react';
import styled from 'styled-components';
import { SlCalender } from 'react-icons/sl';
import { IoPersonCircleSharp } from 'react-icons/io5';
import HeaderDate from './HeaderDate';
import Alert from '../UI/Alert';
import { useNavigate } from 'react-router-dom';
import { useTimerStore } from '../../store/timer';
import { isGuest } from '../../utils/isGuest';
import { useModal } from '../../hooks/useModal';
import LoginModal from '../User/Login/LoginModal';

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

const Button = styled.button`
  color: gray;

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
  const { isOpen, openModal, closeModal } = useModal();
  const [showGoBack, setShowGoBack] = useState(false);

  const handleCalendar = () => {
    if (isGuest()) {
      alert('회원만 이용할 수 있습니다');
    } else {
      if (timerState === 'RUNNING') {
        setShowGoBack(true);
      } else {
        navigate('/tasks');
      }
    }
  };

  const handleMypage = () => {
    if (isGuest()) {
      openModal();
    } else {
      navigate('/mypage');
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
          <Button onClick={handleMypage}>
            <IoPersonCircleSharp style={{ fontSize: '30px' }} />
          </Button>
          <Button onClick={handleCalendar}>
            <SlCalender />
          </Button>
        </Wrapper>
      </Container>
      <LoginModal isOpen={isOpen} closeModal={closeModal} />
      {showGoBack && <Alert text={'task'} getIsShow={getIsShow} />}
    </>
  );
};

export default Header;

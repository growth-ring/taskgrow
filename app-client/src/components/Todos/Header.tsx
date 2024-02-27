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
import { useLogin } from '../../store/login';
import { useTodosStore } from '../../store/todos';

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
    font-size: 15px;
    margin-right: 10px;
  }

  @media (min-width: 768px) and (max-width: 1023px) {
    margin-right: 30px;
  }

  @media (min-width: 1024px) {
    margin-right: 40px;
  }
`;

const TextButton = styled.button`
  color: gray;

  @media (max-width: 767px) {
    font-size: 10px;
  }

  @media (min-width: 768px) and (max-width: 1023px) {
    font-size: 14px;
  }

  @media (min-width: 1024px) {
    font-size: 18px;
  }
`;

const Bar = styled.span`
  width: 1px;
  height: 20px;
  background-color: gray;
`;

const Wrapper = styled.div`
  display: flex;
  align-items: center;
  gap: 1rem;

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
  const { setIsShowLogin } = useLogin();
  const { isOpen, openModal, closeModal } = useModal();
  const [showGoBack, setShowGoBack] = useState(false);
  const guest = isGuest();
  const { offCategory } = useTodosStore();

  const handleCalendar = () => {
    if (timerState === 'RUNNING') {
      setShowGoBack(true);
    } else {
      offCategory();
      navigate('/tasks');
    }
  };

  const handleLogin = () => {
    setIsShowLogin(true);
    openModal();
  };

  const handleSignUp = () => {
    setIsShowLogin(false);
    openModal();
  };

  const handleMypage = () => {
    offCategory();
    navigate('/mypage');
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
          {guest && (
            <>
              <TextButton onClick={handleLogin}>로그인</TextButton>
              <Bar />
              <TextButton onClick={handleSignUp}>회원가입</TextButton>
            </>
          )}
          {!guest && (
            <>
              <Button onClick={handleMypage}>
                <IoPersonCircleSharp style={{ fontSize: '30px' }} />
              </Button>
              <Button onClick={handleCalendar}>
                <SlCalender />
              </Button>
            </>
          )}
        </Wrapper>
      </Container>
      <LoginModal isOpen={isOpen} closeModal={closeModal} />
      {showGoBack && <Alert text={'task'} getIsShow={getIsShow} />}
    </>
  );
};

export default Header;

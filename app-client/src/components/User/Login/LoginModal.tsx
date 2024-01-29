import { styled, css } from 'styled-components';
import Modal from '../../UI/Modal';
import { useLogin } from '../../../store/login';
import Login from './Login';
import SignUp from '../SignUp/SignUp';

interface LoginModalProps {
  isOpen: boolean;
  closeModal: () => void;
}

const LoginModal = ({ isOpen, closeModal }: LoginModalProps) => {
  const { isShowLogin } = useLogin();

  return (
    <StyledWrapper $isOpen={isOpen}>
      <Modal isOpen={isOpen} closeModal={closeModal}>
        <Container>
          {isShowLogin && <Login />}
          {!isShowLogin && <SignUp />}
        </Container>
      </Modal>
    </StyledWrapper>
  );
};

const StyledWrapper = styled.section<{ $isOpen: boolean }>`
  ${(props) =>
    props.$isOpen &&
    css`
      background-color: black;
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      opacity: 0.5;
    `}
`;

const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 360px;
  background-color: rgba(256, 256, 256, 0.5);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  border-radius: 1rem;
`;

export default LoginModal;

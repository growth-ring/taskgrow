import styled from 'styled-components';
import { FcComboChart } from 'react-icons/fc';
import { useNavigate } from 'react-router-dom';

const Main = styled.div`
  width: 60px;
  height: 100%;
  padding-top: 4.5rem;
  display: flex;
  justify-content: center;
  border-left: 1px solid var(--line-color);
`;

const StateButton = styled.button`
  width: 2.5rem;
  height: 2.5rem;
  border-radius: 2rem;

  display: flex;
  align-items: center;
  justify-content: center;

  cursor: pointer;

  &:hover {
    background: var(--line-color);
  }
`;

const StateIcon = styled(FcComboChart)`
  width: 1.8rem;
  height: 1.8rem;
`;

const Menu = () => {
  const navigate = useNavigate();

  const handleGoState = () => {
    navigate('/stats');
  };

  return (
    <Main>
      <StateButton onClick={handleGoState} title="통계보기">
        <StateIcon />
      </StateButton>
    </Main>
  );
};

export default Menu;

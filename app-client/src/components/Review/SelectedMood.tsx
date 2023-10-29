import styled from 'styled-components';

const Score = styled.div`
  background-color: var(--sub-yellow-color);
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  color: var(--main-color);
  margin-bottom: 2rem;
  width: 70px;
  height: 70px;
  font-size: 25px;
`;

const SelectedMood = () => {
  return <Score> ? </Score>;
};

export default SelectedMood;

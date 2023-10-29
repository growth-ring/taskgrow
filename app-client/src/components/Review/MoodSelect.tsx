import styled from 'styled-components';

const Container = styled.div`
  display: flex;
`;

const Square = styled.button`
  width: 30px;
  height: 30px;
  background-color: var(--sub-yellow-color);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 15px;
  color: gray;
`;

const MoodSelect = () => {
  return (
    <Container>
      {Array.from({ length: 10 }, (_, index) => (
        <Square key={index}>{index + 1}</Square>
      ))}
    </Container>
  );
};

export default MoodSelect;

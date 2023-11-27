import styled from 'styled-components';
import Box from '../UI/Box';

const Container = styled.div`
  display: flex;
  justify-content: center;
  text-align: center;

  @media (min-width: 768px) {
    width: 50%;
    flex-direction: column;
  }
`;

const TodosStats = () => {
  return (
    <Container>
      <Box />
    </Container>
  );
};

export default TodosStats;

import styled from 'styled-components';

const Container = styled.div`
  text-align: right;
  color: gray;
  margin-bottom: 0.5rem;
`;

const DeleteButton = styled.button`
  &:hover {
    color: black;
  }
`;

const TodoAllDelete = () => {
  return (
    <Container>
      <DeleteButton>모두 삭제하기</DeleteButton>
    </Container>
  );
};

export default TodoAllDelete;

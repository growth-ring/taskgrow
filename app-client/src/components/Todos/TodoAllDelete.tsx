import { useState } from 'react';
import styled from 'styled-components';
import { useTask } from '../../store/task';
import DeleteAllTodos from './DeleteAllTodos';
import { useTodosStore } from '../../store/todos';

const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  margin-left: auto;
  margin-right: auto;
  max-width: 32rem;
  color: gray;
  margin-bottom: 0.5rem;
`;

const DeleteButton = styled.button`
  margin-top: 10px;
  &:hover {
    color: black;
  }
`;

const TodoAllDelete = () => {
  const { selectedTaskId } = useTask();
  const [isShow, setIsShow] = useState(false);
  const { todoList } = useTodosStore();
  const isTodo = todoList.length > 0;

  const getIsShow = () => {
    setIsShow(false);
  };

  const handleDeleteClick = () => {
    setIsShow(true);
  };

  return (
    <>
      {isTodo && (
        <Container>
          <DeleteButton onClick={handleDeleteClick}>전체 삭제하기</DeleteButton>
        </Container>
      )}
      {isShow && (
        <DeleteAllTodos selectedTaskId={selectedTaskId} getIsShow={getIsShow} />
      )}
    </>
  );
};

export default TodoAllDelete;

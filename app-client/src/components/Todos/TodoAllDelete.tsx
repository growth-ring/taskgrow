import { useState } from 'react';
import styled from 'styled-components';
import { useTask } from '../../store/task';
import DeleteAllTodos from './DeleteAllTodos';
import { useTodosStore } from '../../store/todos';

const Container = styled.div`
  display: flex;
  justify-content: flex-end;
  margin-left: auto;
  margin-right: auto;
  max-width: 32rem;
  color: gray;
  margin-bottom: 0.5rem;
`;

const Button = styled.button`
  margin: 10px 0 0 10px;
  &:hover {
    color: black;
  }
`;

const TodoAllDelete = () => {
  const { selectedTaskId } = useTask();
  const [isShow, setIsShow] = useState(false);
  const { todoList, isCategory, onCategory, offCategory } = useTodosStore();
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
          {!isCategory && <Button onClick={onCategory}>카테고리별 보기</Button>}
          {isCategory && <Button onClick={offCategory}>전체 보기</Button>}
          <Button onClick={handleDeleteClick}>전체 삭제하기</Button>
        </Container>
      )}
      {isShow && (
        <DeleteAllTodos selectedTaskId={selectedTaskId} getIsShow={getIsShow} />
      )}
    </>
  );
};

export default TodoAllDelete;

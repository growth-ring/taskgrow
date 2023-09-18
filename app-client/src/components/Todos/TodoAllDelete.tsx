import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import styled from 'styled-components';
import { useTask } from '../../store/task';
import { deleteTask } from '../../services/task';

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
  &:hover {
    color: black;
  }
`;

const CheckboxLabel = styled.label`
  display: flex;
  align-items: center;
  margin-top: 10px;
`;

const TodoAllDelete = () => {
  const navigate = useNavigate();
  const { selectedTaskId } = useTask();
  const [isChecked, setIsChecked] = useState(false);

  const handleCheckboxChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setIsChecked(e.target.checked);
  };

  const handleDeleteClick = () => {
    //TODO: todo삭제
    if (isChecked) {
      deleteTask(selectedTaskId).then((message) => {
        if (message?.request.status === 204) {
          navigate('/tasks');
        }
      });
    }
  };

  return (
    <Container>
      <DeleteButton onClick={handleDeleteClick}>전체 삭제하기</DeleteButton>
      <CheckboxLabel>
        <input
          type="checkbox"
          checked={isChecked}
          onChange={handleCheckboxChange}
        />
        <p style={{ marginLeft: '5px' }}>task 삭제</p>
      </CheckboxLabel>
    </Container>
  );
};

export default TodoAllDelete;

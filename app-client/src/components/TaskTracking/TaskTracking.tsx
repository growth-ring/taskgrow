import styled from 'styled-components';
import { useTodosStore } from '../../store/todos';
import { calculateTime } from '../../utils/calculateTime';

const TaskBox = styled.div`
  flex: 1;
  text-align: center;
  color: gray;
  font-size: 12px;
`;

const TaskContent = styled.div`
  margin-bottom: 7px;
`;

const HighLight = styled.span`
  color: var(--main-color);
  font-size: 18px;
  font-weight: bold;
`;

const TaskTracking = () => {
  const { todoList } = useTodosStore();

  let plannedTime = 0;
  let unFinishedTodo = 0;
  let completedTime = 0;
  let completedTodo = 0;

  todoList.forEach((todo) => {
    if (todo.status !== 'DONE') {
      plannedTime +=
        todo.planCount > todo.performCount
          ? (todo.planCount - todo.performCount) * 25
          : 0;
      unFinishedTodo += 1;
      completedTime += todo.performCount * 25;
    } else {
      completedTime += todo.performCount * 25;
      completedTodo += 1;
    }
  });

  const { hours: plannedHours, minutes: plannedMinutes } =
    calculateTime(plannedTime);
  const { hours: completedTimeHours, minutes: completedTimeMinutes } =
    calculateTime(completedTime);

  return (
    <div className="max-w-lg mx-auto bg-white p-4 rounded-lg shadow shadow-slate-300 flex mb-4">
      <TaskBox>
        <TaskContent>
          {plannedHours !== 0 && (
            <>
              <HighLight>{plannedHours}</HighLight>시간
            </>
          )}
          <HighLight> {plannedMinutes}</HighLight>분
        </TaskContent>
        <span>예정 시간</span>
      </TaskBox>
      <TaskBox>
        <TaskContent>
          <HighLight>{unFinishedTodo} </HighLight>개
        </TaskContent>
        <span>남은 할 일</span>
      </TaskBox>
      <TaskBox>
        <TaskContent>
          {completedTimeHours !== 0 && (
            <>
              <HighLight>{completedTimeHours}</HighLight>시간
            </>
          )}
          <HighLight> {completedTimeMinutes}</HighLight>분
        </TaskContent>
        <span>완료한 시간</span>
      </TaskBox>
      <TaskBox>
        <TaskContent>
          <HighLight>{completedTodo} </HighLight>개
        </TaskContent>
        <span>완료한 할 일</span>
      </TaskBox>
    </div>
  );
};

export default TaskTracking;

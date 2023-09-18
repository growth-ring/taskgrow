import styled from 'styled-components';
import Calendar from '../../components/Calendar/Calendar';
import { startEndDate } from '../../utils/startEndDate';

const Main = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
`;

const TaskPage = () => {
  const monthDate = startEndDate('today');

  return (
    <Main>
      <Calendar
        thisMonthStart={monthDate.startDate}
        thisMonthEnd={monthDate.endDate}
      />
    </Main>
  );
};

export default TaskPage;

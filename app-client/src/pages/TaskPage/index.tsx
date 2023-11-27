import styled from 'styled-components';
import Calendar from '../../components/Calendar/Calendar';
import Menu from '../../components/Menu/Menu';
import { startEndDate } from '../../utils/startEndDate';

const Main = styled.div`
  display: flex;
  width: 100%;
  height: 100%;
`;

const TaskPage = () => {
  const monthDate = startEndDate(new Date());

  return (
    <Main>
      <Calendar
        thisMonthStart={monthDate.startDate}
        thisMonthEnd={monthDate.endDate}
      />
      <Menu />
    </Main>
  );
};

export default TaskPage;

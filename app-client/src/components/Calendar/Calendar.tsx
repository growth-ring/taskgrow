import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import moment from 'moment';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import './Calendar.css';
import styled from 'styled-components';

import good from '../../assets/good.png';
import { useTodosStore } from '../../store/todos';

const Todo = styled.div`
  width: 50px;
  height: 50px;
  margin-top: 10px;
  font-size: 22px;
  color: black;
  background-color: #f5f5f5;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
`;

const TaskCalendar = () => {
  const navigate = useNavigate();
  const { setToday } = useTodosStore();

  //TODO: task요청시 현재 날짜의 +7 , -7 task 요청
  const handleViewChange = (value) => {
    const today = value.activeStartDate;

    const firstDayOfMonth = new Date(today.getFullYear(), today.getMonth(), 1);

    const sevenDaysAgo = new Date(firstDayOfMonth);
    sevenDaysAgo.setDate(firstDayOfMonth.getDate() - 7);

    const lastDayOfMonth = new Date(today.getFullYear(), today.getMonth() + 1, 0);

    const sevenDaysLater = new Date(lastDayOfMonth);
    sevenDaysLater.setDate(lastDayOfMonth.getDate() + 7);

    console.log('7일 이전 날짜:', sevenDaysAgo.toLocaleDateString());
    console.log('7일 이후 날짜:', sevenDaysLater.toLocaleDateString());
  };

  const mark = ['2023-08-25', '2023-08-31', '2022-11-22', '2022-11-10'];

  const handleTodayClick = (day: Date) => {
    setToday(moment(day).format('YYYY-MM-DD'));
    navigate('/todos');
  };

  return (
    <Calendar
      onClickDay={handleTodayClick}
      locale="ko-KO"
      next2Label={null}
      prev2Label={null}
      formatDay={(locale, date) => moment(date).format('D')}
      onActiveStartDateChange={handleViewChange}
      tileContent={({ date }) => {
        const html: JSX.Element[] = [];

        mark.forEach((day, i) => {
          if (day === moment(date).format('YYYY-MM-DD')) {
            if (day === '2023-08-25') {
              html.push(<img src={good} key={i} />);
            } else {
              html.push(<Todo key={i}>12</Todo>);
            }
          }
        });

        return <div>{html}</div>;
      }}
    />
  );
};

export default TaskCalendar;

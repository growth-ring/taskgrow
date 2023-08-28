import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

import moment from 'moment';
import styled from 'styled-components';

import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import './Calendar.css';

import { useTodosStore } from '../../store/todos';

import good from '../../assets/good.png';

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
  const [value, onChange] = useState<Date>(new Date());
  const day = moment(value).format('YYYY-MM-DD');
  const currDate = new Date();

  const mark = ['2023-08-25', '2023-08-31', '2022-11-22', '2022-11-10'];

  const handleTodayClick = (day) => {
    setToday(moment(day).format('YYYY-MM-DD'));
    navigate('/todos');
  }

  return (
    <Calendar
      onClickDay={handleTodayClick}
      onChange={onChange}
      value={value}
      locale="ko-KO"
      next2Label={null}
      prev2Label={null}
      formatDay={(locale, date) => moment(date).format('D')}
      tileContent={({ date }) => {
        const html = [];

        mark.forEach((day, i) => {
          if (day === moment(date).format('YYYY-MM-DD')) {
            if(day === '2023-08-25') {
              html.push(<img src={good} />);  
            } else {
              html.push(<Todo>12</Todo>);
            }
          }
        });

        return <div>{html}</div>;
      }}
    />
  );
};

export default TaskCalendar;

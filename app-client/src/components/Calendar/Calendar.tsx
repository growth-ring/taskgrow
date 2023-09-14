import { useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react';
import moment from 'moment';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import './Calendar.css';
import styled from 'styled-components';
import { startEndDate } from '../../utils/startEndDate';
import { useUser } from '../../store/user';
import { TaskDate, moveToTask } from '../../utils/checkTaskExists';
import { getTaskList } from '../../services/task';
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

interface ThisMonthProps {
  thisMonthStart: string;
  thisMonthEnd: string;
}

const TaskCalendar = ({ thisMonthStart, thisMonthEnd }: ThisMonthProps) => {
  const navigate = useNavigate();
  const { userId } = useUser();
  const [startDate, setStartDate] = useState(thisMonthStart);
  const [endDate, setEndDate] = useState(thisMonthEnd);
  const [monthTaskDate, setMonthTaskDate] = useState<TaskDate[]>([]);
  const [viewTaskDate, setViewTaskDate] = useState<string[]>([]);

  const handleTodayClick = (day: Date) => {
    const userClickDay = moment(day).format('YYYY-MM-DD');
    const checkTask = { userId, monthTaskDate, userClickDay };
    moveToTask(checkTask);
    navigate(`/todos/${userClickDay}`);
  };

  const handleViewChange = (value: any) => {
    const monthDate = startEndDate(value.activeStartDate);
    setStartDate(monthDate.startDate);
    setEndDate(monthDate.endDate);
  };

  useEffect(() => {
    const taskData = { userId, startDate, endDate };
    getTaskList(taskData).then((tasks) => {
      const updatedData = tasks.map((task: any) => ({
        taskId: task.task_id,
        taskDate: task.task_date.replace('T00:00:00', ''),
        todos: task.todos,
      }));
      setMonthTaskDate(updatedData);
    });
  }, [userId, startDate, endDate]);

  useEffect(() => {
    if (monthTaskDate.length) {
      const filteredTaskDates = monthTaskDate
        .filter((date) => date.todos.remain >= 0)
        .map((date) => date.taskDate);
      setViewTaskDate(filteredTaskDates);
    }
  }, [monthTaskDate]);

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
        const currentDate = moment(date).format('YYYY-MM-DD');

        viewTaskDate.forEach((day, i) => {
          if (day === currentDate) {
            if (
              monthTaskDate
                .filter((dates: any) => dates.taskDate === day)
                .map((date: any) => date.todos.remain === date.todos.done)
            ) {
              html.push(<img src={good} key={i} />);
            } else {
              html.push(
                <Todo key={i}>
                  {monthTaskDate
                    .filter((dates: any) => dates.taskDate === day)
                    .map((date: any) => date.todos.remain)}
                </Todo>,
              );
            }
          }
        });

        return <div>{html}</div>;
      }}
    />
  );
};

export default TaskCalendar;

import { useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react';
import moment from 'moment';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import './Calendar.css';
import styled from 'styled-components';
import { startEndDate } from '../../utils/startEndDate';
import { useUser } from '../../store/user';
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

interface TaskData {
  taskId: string;
  taskDate: string;
  todos: {
    remain: number;
    done: number;
  };
}

interface ThisMonthProps {
  thisMonthStart: string;
  thisMonthEnd: string;
}

const TaskCalendar = ({ thisMonthStart, thisMonthEnd }: ThisMonthProps) => {
  const navigate = useNavigate();
  const { userId } = useUser();
  const [startDate, setStartDate] = useState(thisMonthStart);
  const [endDate, setEndDate] = useState(thisMonthEnd);
  const [monthTaskDate, setMonthTaskDate] = useState<TaskData[]>([]);
  const [viewTaskDate, setViewTaskDate] = useState<string[]>([]);

  const handleTodayClick = (day: Date) => {
    const userClickDay = moment(day).format('YYYY-MM-DD');
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
            const matchingTasks = monthTaskDate.filter(
              (date) => date.taskDate === day,
            );
            const allTasksDone = matchingTasks.every(
              (date) => date.todos.remain === date.todos.done,
            );

            if (allTasksDone) {
              html.push(<img src={good} key={i} />);
            } else {
              const remainingTasks = matchingTasks.reduce(
                (total, date) => total + date.todos.remain,
                0,
              );
              html.push(<Todo key={i}>{remainingTasks}</Todo>);
            }
          }
        });

        return <div>{html}</div>;
      }}
    />
  );
};

export default TaskCalendar;

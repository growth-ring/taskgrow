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
import { useTask } from '../../store/task';

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
  const { setSelectedTaskId } = useTask();
  const [startDate, setStartDate] = useState(thisMonthStart);
  const [endDate, setEndDate] = useState(thisMonthEnd);
  const [monthTaskDate, setMonthTaskDate] = useState<TaskDate[]>([]);
  const [viewTaskDate, setViewTaskDate] = useState<string[]>([]);
  const [mouseOverDay, setMouseOverDay] = useState('');
  const [taskTodos, setTaskTodos] = useState([]);

  const handleTodayClick = (day: Date) => {
    const userClickDay = moment(day).format('YYYY-MM-DD');
    const taskId = moveToTask({ userId, monthTaskDate, userClickDay });
    if (typeof taskId === 'number') {
      setSelectedTaskId(taskId);
      navigate(`/todos/${userClickDay}`);
    } else {
      taskId.then((id) => {
        setSelectedTaskId(id);
        navigate(`/todos/${userClickDay}`);
      });
    }
  };

  const handleDateViewChange = (value: any) => {
    const monthDate = startEndDate(value.activeStartDate);
    setStartDate(monthDate.startDate);
    setEndDate(monthDate.endDate);
  };

  const handleTaskViewChange = (currentDate: string) => {
    setMouseOverDay(currentDate);
  };

  useEffect(() => {
    const taskData = { userId, startDate, endDate };
    getTaskList(taskData).then((tasks) => {
      const updatedData = tasks.map((task: any) => ({
        taskId: task.task_id,
        taskDate: task.task_date,
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
      onActiveStartDateChange={handleDateViewChange}
      tileContent={({ date }) => {
        const html: JSX.Element[] = [];
        const currentDate = moment(date).format('YYYY-MM-DD');
        const checkTask = { userId, monthTaskDate, userClickDay: currentDate };

        viewTaskDate.forEach((day, i) => {
          const taskFinished = monthTaskDate
            .filter((dates: any) => dates.taskDate === day)
            .map((date: any) => date.todos.remain !== date.todos.done);

          if (day === currentDate) {
            if (taskFinished) {
              html.push(<img src={good} key={i} />);
            } else if (mouseOverDay === currentDate) {
              moveToTask(checkTask).then((task) =>
                task.map((it: any) => console.log(it.todos)),
              );
              html.push(
                <div>
                  {taskTodos.map((todo) => (
                    <div>{todo}</div>
                  ))}
                </div>,
              );
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

        return (
          <div
            onMouseOver={() => {
              handleTaskViewChange(currentDate);
            }}
            onMouseOut={() => {
              setMouseOverDay('');
            }}
          >
            {html}
          </div>
        );
      }}
    />
  );
};

export default TaskCalendar;

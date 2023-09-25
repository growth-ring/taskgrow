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
import { getTask, getTaskList } from '../../services/task';
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

const PreviewTodoList = styled.div`
  display: flex;
  width: 100px;
  height: 66px;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

const PreviewTodo = styled.div`
  margin-top: 0.2rem;
`;

interface ThisMonthProps {
  thisMonthStart: string;
  thisMonthEnd: string;
}

interface Todos {
  remain: number;
  done: number;
}

interface TaskProps {
  task_date: string;
  task_id: number;
  todos: Todos;
  user_id: number;
}

const TaskCalendar = ({ thisMonthStart, thisMonthEnd }: ThisMonthProps) => {
  const navigate = useNavigate();
  const { userId } = useUser();
  const { monthTaskDate, setMonthTaskDate, setSelectedTaskId } = useTask();
  const [startDate, setStartDate] = useState(thisMonthStart);
  const [endDate, setEndDate] = useState(thisMonthEnd);
  const [viewTaskDate, setViewTaskDate] = useState<string[]>([]);
  const [mouseOverDay, setMouseOverDay] = useState('');

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
      const updatedData = tasks.map((task: TaskProps) => {
        return getTask(task.task_id).then((todo) => ({
          taskId: task.task_id,
          taskDate: task.task_date,
          todos: task.todos,
          todoData: todo.todos,
        }));
      });

      Promise.all(updatedData).then((data) => {
        setMonthTaskDate(data);
      });
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

        viewTaskDate.forEach((day, i) => {
          const taskFinished = monthTaskDate
            .filter((dates: any) => dates.taskDate === day)
            .map(
              (date: any) =>
                date.todos.remain === date.todos.done &&
                date.todos.remain !== 0,
            );
          if (day === currentDate) {
            if (taskFinished[0]) {
              html.push(<img src={good} key={i} />);
            } else if (mouseOverDay === currentDate) {
              const taskTodo = monthTaskDate
                .filter((dates: any) => dates.taskDate === day)
                .map((date: any) => date.todoData);
              if (taskTodo) {
                html.push(
                  <PreviewTodoList>
                    {taskTodo[0].map((todo: string) => (
                      <PreviewTodo>{todo}</PreviewTodo>
                    ))}
                  </PreviewTodoList>,
                );
              }
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

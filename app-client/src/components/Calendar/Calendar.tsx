import { useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react';
import moment from 'moment';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import './Calendar.css';
import styled from 'styled-components';
import { startEndDate } from '../../utils/startEndDate';
import { useUser } from '../../store/user';
import { getAllTask, clickTask } from '../../utils/checkTaskExists';
import { useTask } from '../../store/task';
import done from '../../assets/done.png';
import FeelingsScore from './FeelingsScore';
import { useTimerStore } from '../../store/timer';
import { useTodosStore } from '../../store/todos';

const Todo = styled.div`
  color: black;
  background-color: #f5f5f5;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;

  @media (max-width: 767px) {
    margin: 10px;
    width: 40px;
    height: 40px;
    font-size: 18px;
  }

  @media (min-width: 768px) and (max-width: 1023px) {
    margin: 10px;
    width: 45px;
    height: 45px;
    font-size: 20px;
  }

  @media (min-width: 1024px) {
    margin: 10px;
    width: 50px;
    height: 50px;
    font-size: 22px;
  }
`;

const PreviewTodoList = styled.div`
  text-align: left;
  width: 100%;
  overflow: hidden;

  @media (max-width: 767px) {
    font-size: 9px;
  }

  @media (min-width: 768px) and (max-width: 1023px) {
    font-size: 11px;
  }

  @media (min-width: 1024px) {
    font-size: 13px;
  }
`;

const PreviewTodo = styled.div`
  color: black;

  @media (min-width: 768px) and (max-width: 1023px) {
    margin-top: 0.3rem;
  }

  @media (min-width: 1024px) {
    margin-top: 0.4rem;
  }
`;

const TodoText = styled.div`
  display: inline-block;
  vertical-align: middle;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;

  @media (max-width: 767px) {
    width: 1.5rem;
    height: 1rem;
  }

  @media (min-width: 768px) and (max-width: 1023px) {
    width: 3.6rem;
    height: 1rem;
  }

  @media (min-width: 1024px) {
    width: 5.5rem;
    height: 1rem;
  }
`;

interface ThisMonthProps {
  thisMonthStart: string;
  thisMonthEnd: string;
}

const TaskCalendar = ({ thisMonthStart, thisMonthEnd }: ThisMonthProps) => {
  const navigate = useNavigate();
  const { userId } = useUser();
  const { stop } = useTimerStore();
  const { monthTaskDate, setMonthTaskDate, setSelectedTaskId } = useTask();
  const { setTaskDate } = useTodosStore();
  const [startDate, setStartDate] = useState(thisMonthStart);
  const [endDate, setEndDate] = useState(thisMonthEnd);
  const [viewTaskDate, setViewTaskDate] = useState<string[]>([]);
  const [mouseOverDay, setMouseOverDay] = useState('');

  const handleTodayClick = async (day: Date) => {
    stop();
    const taskDate = moment(day).format('YYYY-MM-DD');
    const taskId = await clickTask({ userId, monthTaskDate, taskDate });
    setSelectedTaskId(taskId);
    setTaskDate(taskDate);
    localStorage.setItem('taskDate', taskDate);
    localStorage.setItem('taskId', String(taskId));
    localStorage.setItem('todo', '');
    navigate(`/todos/${taskDate}`);
  };

  const handleDateViewChange = ({ activeStartDate }: any) => {
    if (activeStartDate === null) {
      console.error('activeStartDate는 null이 될 수 없습니다.');
      return;
    }

    const monthDate = startEndDate(activeStartDate);
    setStartDate(monthDate.startDate);
    setEndDate(monthDate.endDate);
  };

  const handleTaskViewChange = (currentDate: string) => {
    setMouseOverDay(currentDate);
  };

  useEffect(() => {
    const taskData = { userId, startDate, endDate };
    const getMonthTaskDate = async () => {
      setMonthTaskDate(await getAllTask(taskData));
    };
    getMonthTaskDate();
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
      formatDay={(locale, date) => moment(date, locale).format('D')}
      onActiveStartDateChange={handleDateViewChange}
      tileContent={({ date }) => {
        let html: JSX.Element | null = null;
        const currentDate = moment(date).format('YYYY-MM-DD');

        viewTaskDate.forEach((day, i) => {
          const score = monthTaskDate.filter(
            (dates) => dates.taskDate === day,
          )[0]?.feelingsScore;

          const taskFinished = monthTaskDate
            .filter((dates) => dates.taskDate === day)
            .map((date) => date.todos.remain === 0 && date.todos.done !== 0);

          const hasTodos =
            monthTaskDate.filter(
              (dates) => dates.taskDate === day && dates.todoData.length > 0,
            ).length > 0;

          if (day === currentDate) {
            if (taskFinished[0] && score === -1) {
              html = (
                <Todo>
                  <img src={done} />
                </Todo>
              );
            } else if (taskFinished[0]) {
              html = <FeelingsScore score={score} />;
            } else if (mouseOverDay === currentDate) {
              const taskTodo = monthTaskDate
                .filter((dates) => dates.taskDate === day)
                .map((date) => date.todoData.map((todo) => todo.todo));

              const taskTodoCount = monthTaskDate
                .filter((dates) => dates.taskDate === day)
                .map((date) =>
                  date.todoData.map(
                    (todo) => `${todo.performCount} / ${todo.planCount}`,
                  ),
                );

              if (taskTodo) {
                html = (
                  <PreviewTodoList key={i}>
                    {taskTodo[0].map((todo: string, index: number) => (
                      <PreviewTodo key={`${i}_${index}`}>
                        ◻ <TodoText>{todo}</TodoText> {taskTodoCount[0][index]}
                      </PreviewTodo>
                    ))}
                  </PreviewTodoList>
                );
              }
            } else if (hasTodos) {
              html = (
                <Todo key={i}>
                  {monthTaskDate
                    .filter((dates) => dates.taskDate === day)
                    .map((date) => date.todos.remain)}
                </Todo>
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

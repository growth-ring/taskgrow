import { getTask, addTask } from '../services/task';

export interface TaskDate {
  taskId: string;
  taskDate: string;
  todos: {
    remain: number;
    done: number;
  };
}

interface TaskExistsProps {
  monthTaskDate: TaskDate[];
  userClickDay: string;
}

interface TaskProps extends TaskExistsProps {
  userId: number;
}

const checkTaskExists = (checkForm: TaskExistsProps) => {
  return (
    checkForm.monthTaskDate.filter(
      (task) => task.taskDate === checkForm.userClickDay,
    ).length === 1
  );
};

export const moveToTask = ({
  userId,
  monthTaskDate,
  userClickDay,
}: TaskProps) => {
  const checkForm = { monthTaskDate, userClickDay };
  if (checkTaskExists(checkForm)) {
    const taskId = Number(
      monthTaskDate
        .filter((task) => task.taskDate === userClickDay)
        .map((task) => task.taskId),
    );
    getTask(taskId);
  } else {
    const dayTaskDate = { userId, userClickDay };
    addTask(dayTaskDate);
  }
};

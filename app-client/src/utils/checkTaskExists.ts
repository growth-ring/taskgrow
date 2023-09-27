import { addTask } from '../services/task';
import { getTaskList, getTask } from '../services/task';

export interface TaskDate {
  taskId: string;
  taskDate: string;
  todos: {
    remain: number;
    done: number;
  };
}

interface TaskProps {
  task_id: number;
  task_date: string;
  todos: {
    remain: number;
    done: number;
  };
  user_id: number;
}

interface TaskExistsProps {
  monthTaskDate: TaskDate[];
  userClickDay: string;
}

interface MoveToTaskProps extends TaskExistsProps {
  userId: number;
}

interface AllTaskProps {
  userId: number;
  startDate: string;
  endDate: string;
}

const checkTaskExists = (checkForm: TaskExistsProps) => {
  return (
    checkForm.monthTaskDate.filter(
      (task) => task.taskDate === checkForm.userClickDay,
    ).length === 1
  );
};

const getTaskId = ({ monthTaskDate, userClickDay }: TaskExistsProps) => {
  const taskId = Number(
    monthTaskDate
      .filter((task) => task.taskDate === userClickDay)
      .map((task) => task.taskId),
  );
  return taskId;
};

export const getAllTask = async ({
  userId,
  startDate,
  endDate,
}: AllTaskProps) => {
  const taskData = { userId, startDate, endDate };
  const tasks = await getTaskList(taskData);

  const updatedData = await Promise.all(
    tasks.map(async (task: TaskProps) => {
      const todo = await getTask(task.task_id);
      return {
        taskId: task.task_id,
        taskDate: task.task_date,
        todos: task.todos,
        todoData: todo.todos,
      };
    }),
  );

  return updatedData;
};

export const moveToTask = async ({
  userId,
  monthTaskDate,
  userClickDay,
}: MoveToTaskProps): Promise<number> => {
  const checkForm = { monthTaskDate, userClickDay };
  if (checkTaskExists(checkForm)) {
    return getTaskId(checkForm);
  } else {
    return addTask({ userId, userClickDay });
  }
};

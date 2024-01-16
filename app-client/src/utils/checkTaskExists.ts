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

interface TaskData {
  taskId: number;
  taskDate: string;
  todos: {
    remain: number;
    done: number;
  };
  feelingsScore: number;
  userId: number;
}

interface TaskExistProps {
  monthTaskDate: TaskDate[];
  taskDate: string;
}

interface ClickTaskProps extends TaskExistProps {
  userId: number;
}

interface AllTaskProps {
  userId: number;
  startDate: string;
  endDate: string;
}

export const getAllTask = async ({
  userId,
  startDate,
  endDate,
}: AllTaskProps) => {
  const taskData = { userId, startDate, endDate };
  const tasks = await getTaskList(taskData);

  const updatedData = await Promise.all(
    tasks.map(async (task: TaskData) => {
      const todo = await getTask(task.taskId);
      return {
        taskId: task.taskId,
        taskDate: task.taskDate,
        todos: task.todos,
        feelingsScore: task.feelingsScore,
        todoData: todo.todos,
      };
    }),
  );

  return updatedData;
};

export const clickTask = async ({
  userId,
  monthTaskDate,
  taskDate,
}: ClickTaskProps): Promise<number> => {
  const checkForm = { monthTaskDate, taskDate };
  if (isTaskExist(checkForm)) {
    return getTaskId(checkForm);
  } else {
    return addTask({ userId, taskDate });
  }
};

const isTaskExist = (checkForm: TaskExistProps) => {
  return checkForm.monthTaskDate.some(
    (task) => task.taskDate === checkForm.taskDate,
  );
};

const getTaskId = ({ monthTaskDate, taskDate }: TaskExistProps) => {
  const foundTask = monthTaskDate.find((task) => task.taskDate === taskDate);
  return Number(foundTask?.taskId);
};

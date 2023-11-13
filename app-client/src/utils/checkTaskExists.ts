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
  task_id: number;
  task_date: string;
  todos: {
    remain: number;
    done: number;
  };
  feelings_score: number;
  user_id: number;
}

interface TaskExistProps {
  monthTaskDate: TaskDate[];
  userClickDay: string;
}

interface ClickTaskProps extends TaskExistProps {
  userId: number;
}

interface AllTaskProps {
  userId: number;
  startDate: string;
  endDate: string;
}

const isTaskExist = (checkForm: TaskExistProps) => {
  return (
    checkForm.monthTaskDate.filter(
      (task) => task.taskDate === checkForm.userClickDay,
    ).length === 1
  );
};

const getTaskId = ({ monthTaskDate, userClickDay }: TaskExistProps) => {
  const foundTask = monthTaskDate.find(
    (task) => task.taskDate === userClickDay,
  );
  return Number(foundTask?.taskId);
};

export const getAllTask = async ({
  userId,
  startDate,
  endDate,
}: AllTaskProps) => {
  const taskData = { userId, startDate, endDate };
  const tasks = await getTaskList(taskData);

  const updatedData = await Promise.all(
    tasks.map(async (task: TaskData) => {
      const todo = await getTask(task.task_id);
      return {
        taskId: task.task_id,
        taskDate: task.task_date,
        todos: task.todos,
        feelingsScore: task.feelings_score,
        todoData: todo.todos,
      };
    }),
  );

  return updatedData;
};

export const clickTask = async ({
  userId,
  monthTaskDate,
  userClickDay,
}: ClickTaskProps): Promise<number> => {
  const checkForm = { monthTaskDate, userClickDay };
  if (isTaskExist(checkForm)) {
    return getTaskId(checkForm);
  } else {
    return addTask({ userId, userClickDay });
  }
};

import { create } from 'zustand';
import { useDate } from './stats';
import { useUser } from './user';
import { getTodosStats, getTodosDetail } from '../services/mypage';
import moment from 'moment';

export interface Todo {
  todoId: number;
  taskId: number;
  todo: string;
  status: string;
  planCount: number;
  performCount: number;
  orderNo?: number;
}

interface TodosStats {
  totalCount: number;
  doneCount: number;
  progressCount: number;
  undoneCount: number;
}

interface TodoDetail {
  todo: string;
  status: string;
  performCount: number;
  planCount: number;
  taskDate: string;
}

export interface TodosStore {
  taskDate: string;
  setTaskDate: (running: string) => void;
  todoList: Todo[];
  setTodoList: (todos: Todo[]) => void;
  todoId: number;
  setTodoId: (todoId: number) => void;
  performCount: number;
  setPerformCount: (performCount: number) => void;
  planCount: number;
  setPlanCount: (planCount: number) => void;
  isTodoChange: boolean;
  setIsTodoChange: (todoChange: boolean) => void;
  selectedTodo: string;
  setSelectedTodo: (todo: string) => void;
  todosStats: TodosStats;
  getTodos: () => void;
  todoDetail: TodoDetail[];
  getTodoDetail: ({ status, page }: { status: string; page: number }) => void;
}

const userTaskDate = localStorage.getItem('taskDate');
const userSelectedTodo = localStorage.getItem('todo');
const userTodoId = Number(localStorage.getItem('todoId'));

export const useTodosStore = create<TodosStore>((set) => ({
  taskDate: userTaskDate ? userTaskDate : '',
  setTaskDate: (day) => set({ taskDate: day }),
  todoList: [],
  setTodoList: (newTodo) => set({ todoList: newTodo }),
  todoId: userTodoId ? userTodoId : 0,
  setTodoId: (id) => set({ todoId: id }),
  performCount: 0,
  setPerformCount: (count) => set({ performCount: count }),
  planCount: 0,
  setPlanCount: (count) => set({ planCount: count }),
  isTodoChange: false,
  setIsTodoChange: (todoChange) => set({ isTodoChange: todoChange }),
  selectedTodo: userSelectedTodo
    ? userSelectedTodo
    : '오늘 할 일 추가해 주세요',
  setSelectedTodo: (todo) => set({ selectedTodo: todo }),
  todosStats: {
    totalCount: 0,
    doneCount: 0,
    progressCount: 0,
    undoneCount: 0,
  },
  getTodos: async () => {
    const { year, month } = useDate.getState();
    const { userId } = useUser.getState();

    const firstDay = moment(new Date(year, month - 1, 1)).format('YYYY-MM-DD');
    const lastDay = moment(new Date(year, month, 0)).format('YYYY-MM-DD');

    const userTodosStats = {
      userId: userId.toString(),
      startDate: firstDay,
      endDate: lastDay,
    };

    const todos = await getTodosStats(userTodosStats);
    set({ todosStats: todos });
  },
  todoDetail: [],
  getTodoDetail: async ({ status, page }) => {
    const { year, month } = useDate.getState();
    const { userId } = useUser.getState();

    const firstDay = moment(new Date(year, month - 1, 1)).format('YYYY-MM-DD');
    const lastDay = moment(new Date(year, month, 0)).format('YYYY-MM-DD');

    const userTodos = {
      userId: userId.toString(),
      status: status,
      startDate: firstDay,
      endDate: lastDay,
      page: page,
    };

    const detailContent = await getTodosDetail(userTodos);

    set(() => {
      return { todoDetail: detailContent };
    });
  },
}));

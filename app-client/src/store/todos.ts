import { create } from 'zustand';
import { useDate } from './stats';
import { useUser } from './user';
import { getTodosStats } from '../services/mypage';
import moment from 'moment';

export interface Todo {
  todo_id: number;
  task_id: number;
  todo: string;
  status: string;
  plan_count: number;
  perform_count: number;
}

interface TodosStats {
  total_count: number;
  done_count: number;
  progress_count: number;
  undone_count: number;
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
}

const userTaskDate = localStorage.getItem('taskDate');
const userSelectedTodo = localStorage.getItem('todo');

export const useTodosStore = create<TodosStore>((set) => ({
  taskDate: userTaskDate ? userTaskDate : '',
  setTaskDate: (day) => set({ taskDate: day }),
  todoList: [],
  setTodoList: (newTodo) => set({ todoList: newTodo }),
  todoId: 0,
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
    total_count: 0,
    done_count: 0,
    progress_count: 0,
    undone_count: 0,
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
}));

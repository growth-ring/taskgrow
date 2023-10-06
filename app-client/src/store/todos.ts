import { create } from 'zustand';

interface Todo {
  todo_id: number;
  task_id: number;
  todo: string;
  status: string;
  plan_count: number;
  perform_count: number;
}

interface TodosStore {
  today: string;
  setToday: (running: string) => void;
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
}

export const useTodosStore = create<TodosStore>((set) => ({
  today: '',
  setToday: (day) => set({ today: day }),
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
  selectedTodo: '오늘 할 일 골라주세요',
  setSelectedTodo: (todo) => set({ selectedTodo: todo }),
}));

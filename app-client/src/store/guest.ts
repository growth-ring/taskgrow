import { create } from 'zustand';

interface Todo {
  todo: string;
  status: string;
  performCount: number;
  planCount: number;
  todoId: number;
  taskId: number;
}

interface GuestStore {
  todoListId: number;
  incrementTodoListId: () => void;
  todoList: Todo[];
  guestAddTodo: (newTodo: Todo) => void;
  incrementPerformCount: (todoId: number) => void;
  updatePlanCount: (todoId: number, newPlanCount: number) => void;
  updateTodoStatus: (todoId: number, newStatus: string) => void;
  updateGuestTodo: (todoId: number, newTodo: string) => void;
  resetTodoList: () => void;
  removeTodo: (todoId: number) => void;
}

export const useGuestStore = create<GuestStore>((set) => ({
  todoListId: 1,
  incrementTodoListId: () =>
    set((state) => ({ todoListId: state.todoListId + 1 })),
  todoList: [],
  guestAddTodo: (newTodo: Todo) =>
    set((state) => ({ todoList: [...state.todoList, newTodo] })),
  incrementPerformCount: (todoId: number) =>
    set((state) => ({
      todoList: state.todoList.map((item) =>
        item.todoId === todoId
          ? { ...item, performCount: item.performCount + 1 }
          : item,
      ),
    })),
  updatePlanCount: (todoId: number, newPlanCount: number) =>
    set((state) => ({
      todoList: state.todoList.map((item) =>
        item.todoId === todoId ? { ...item, planCount: newPlanCount } : item,
      ),
    })),
  updateTodoStatus: (todoId: number, newStatus: string) =>
    set((state) => ({
      todoList: state.todoList.map((item) =>
        item.todoId === todoId ? { ...item, status: newStatus } : item,
      ),
    })),
  updateGuestTodo: (todoId: number, newTodo: string) =>
    set((state) => ({
      todoList: state.todoList.map((item) =>
        item.todoId === todoId ? { ...item, todo: newTodo } : item,
      ),
    })),
  resetTodoList: () => set({ todoList: [] }),
  removeTodo: (todoId: number) =>
    set((state) => ({
      todoList: state.todoList.filter((todo) => todo.todoId !== todoId),
    })),
}));

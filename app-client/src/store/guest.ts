import { create } from 'zustand';

interface Todo {
  todo: string;
  status: string;
  performCount: number;
  planCount: number;
  todoId: number;
  taskId: number;
  orderNo: number;
}

interface GuestStore {
  todoListId: number;
  incrementTodoListId: () => void;
  guestTodoList: Todo[];
  guestAddTodo: (newTodo: Todo) => void;
  incrementPerformCount: (todoId: number) => void;
  updatePlanCount: (todoId: number, newPlanCount: number) => void;
  updateTodoStatus: (todoId: number, newStatus: string) => void;
  updateTodoOrderNo: (todoId: number, orderNo: number) => void;
  updateGuestTodo: (todoId: number, newTodo: string) => void;
  resetTodoList: () => void;
  removeTodo: (todoId: number) => void;
}

export const useGuestStore = create<GuestStore>((set) => ({
  todoListId: 1,
  incrementTodoListId: () =>
    set((state) => ({ todoListId: state.todoListId + 1 })),
  guestTodoList: [],
  guestAddTodo: (newTodo: Todo) =>
    set((state) => ({ guestTodoList: [...state.guestTodoList, newTodo] })),
  incrementPerformCount: (todoId: number) =>
    set((state) => ({
      guestTodoList: state.guestTodoList.map((item) =>
        item.todoId === todoId
          ? { ...item, performCount: item.performCount + 1 }
          : item,
      ),
    })),
  updatePlanCount: (todoId: number, newPlanCount: number) =>
    set((state) => ({
      guestTodoList: state.guestTodoList.map((item) =>
        item.todoId === todoId ? { ...item, planCount: newPlanCount } : item,
      ),
    })),
  updateTodoStatus: (todoId: number, newStatus: string) =>
    set((state) => ({
      guestTodoList: state.guestTodoList.map((item) =>
        item.todoId === todoId ? { ...item, status: newStatus } : item,
      ),
    })),
  updateTodoOrderNo: (todoId: number, orderNo: number) =>
    set((state) => ({
      guestTodoList: state.guestTodoList.map((item) =>
        item.todoId === todoId ? { ...item, orderNo: orderNo } : item,
      ),
    })),
  updateGuestTodo: (todoId: number, newTodo: string) =>
    set((state) => ({
      guestTodoList: state.guestTodoList.map((item) =>
        item.todoId === todoId ? { ...item, todo: newTodo } : item,
      ),
    })),
  resetTodoList: () => set({ guestTodoList: [] }),
  removeTodo: (todoId: number) =>
    set((state) => ({
      guestTodoList: state.guestTodoList.filter(
        (todo) => todo.todoId !== todoId,
      ),
    })),
}));

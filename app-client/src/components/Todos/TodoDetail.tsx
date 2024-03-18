import { useState, useEffect } from 'react';
import { updateTodo } from '../../services/todo';
import { useTodosStore } from '../../store/todos';
import { isGuest } from '../../utils/isGuest';
import { useGuestStore } from '../../store/guest';
import { getCategory } from '../../services/category';

interface TodoProps {
  todoId: number;
  todoTitle: string;
  todoStatus: string;
  todoPlanCount: number;
  todoPerformCount: number;
  todoCategory: string | number | null;
  getIsShow: () => void;
}

interface CategoriesType {
  id: number;
  name: string;
}

const TodoDetail = ({
  todoId,
  todoTitle,
  todoStatus,
  todoPlanCount,
  todoPerformCount,
  todoCategory,
  getIsShow,
}: TodoProps) => {
  const { isTodoChange, setIsTodoChange } = useTodosStore();
  const { updatePlanCount, updateTodoStatus, updateGuestTodo } =
    useGuestStore();
  const [todo, setTodo] = useState(todoTitle);
  const [planCount, setPlanCount] = useState(todoPlanCount);
  const [category, setCategory] = useState(todoCategory);
  const [categories, setCategories] = useState<CategoriesType[]>();
  const [categoryId, setCategoryId] = useState(0);

  const handleClose = () => {
    getIsShow();
  };

  const handleUpdateTodo = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const todoData = {
      todoId: todoId,
      todo: todo,
      status: todoStatus,
      planCount: planCount,
      categoryId: categoryId,
    };
    if (+planCount > 0 && +planCount <= 20) {
      if (isGuest()) {
        updatePlanCount(todoId, planCount);
        updateTodoStatus(todoId, todoStatus);
        updateGuestTodo(todoId, todo);
        getIsShow();
        setIsTodoChange(!isTodoChange);
      } else {
        updateTodo(todoData).then(() => {
          getIsShow();
          setIsTodoChange(!isTodoChange);
        });
      }
    } else if (+planCount <= 0) {
      alert('1 이상의 숫자를 넣어주세요');
    } else {
      alert('뽀모도로는 최대 20개까지 가능합니다');
    }
  };

  const handleTodoChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setTodo(e.target.value);
  };

  const handlePlanCountChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPlanCount(+e.target.value);
  };

  const handleSelectChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const text = categories?.find((category) => category.id === +e.target.value)
      ?.name;
    setCategory(text!);
    setCategoryId(+e.target.value);
  };

  useEffect(() => {
    const categoryData = async () => {
      const categoryList = await getCategory();
      if (category !== null) {
        categoryList.push({
          id: 0,
          name: '카테고리 없음',
        });
      }
      setCategories(categoryList);
    };

    categoryData();
  }, [category]);

  return (
    <div>
      <div
        className="fixed inset-0 z-50 overflow-y-auto"
        aria-labelledby="modal-title"
        role="dialog"
        aria-modal="true"
      >
        <div className="flex items-end justify-center min-h-screen px-4 text-center md:items-center sm:block sm:p-0">
          <div
            onClick={handleClose}
            className="fixed inset-0 transition-opacity bg-gray-500 bg-opacity-40"
            aria-hidden="true"
          ></div>

          <div className="inline-block w-full max-w-xl p-8 my-20 overflow-hidden text-left transition-all transform bg-white rounded-lg shadow-xl 2xl:max-w-2xl">
            <div className="flex items-center justify-between space-x-4">
              <h1 className="text-xl font-medium ">할 일 수정</h1>

              <button onClick={handleClose}>
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  className="w-6 h-6"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth="2"
                    d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z"
                  />
                </svg>
              </button>
            </div>

            <div className="mt-2 text-sm text-gray-500">
              <hr />
            </div>

            <form className="mt-5" onSubmit={handleUpdateTodo}>
              <div>
                <label
                  htmlFor="todo"
                  className="block text-sm capitalize dark:text-gray-200"
                >
                  할 일
                </label>
                <input
                  id="todo"
                  name="todo"
                  type="text"
                  required
                  placeholder=""
                  maxLength={50}
                  value={todo}
                  onChange={handleTodoChange}
                  className="block w-full px-3 py-2 mt-2 text-gray-600 placeholder-gray-400 bg-white border border-gray-200 rounded-md focus:border-indigo-400 focus:outline-none focus:ring focus:ring-indigo-300 focus:ring-opacity-40"
                />
              </div>

              <div className="mt-4">
                <label
                  htmlFor="category"
                  className="block text-sm capitalize dark:text-gray-200"
                >
                  카테고리
                </label>
                <select
                  onChange={handleSelectChange}
                  className="block w-full px-3 py-2 mt-2 text-gray-600 placeholder-gray-400 bg-white border border-gray-200 rounded-md focus:border-indigo-400 focus:outline-none focus:ring focus:ring-indigo-300 focus:ring-opacity-40"
                >
                  <option value="">
                    {category === null ? '카테고리 없음' : category}
                  </option>
                  {categories?.map((newCategory) => {
                    if (newCategory.name !== category) {
                      return (
                        <option key={newCategory.id} value={newCategory.id}>
                          {newCategory.name}
                        </option>
                      );
                    }
                  })}
                </select>
              </div>

              <div className="mt-4">
                <label
                  htmlFor="performCount"
                  className="block text-sm text-gray-700 capitalize dark:text-gray-200 "
                >
                  완료 뽀모도로 개수
                </label>
                <input
                  id="performCount"
                  name="performCount"
                  type="number"
                  disabled
                  value={todoPerformCount}
                  className="block w-full px-3 py-2 mt-2 text-gray bg-lightGray border border-gray-200 rounded-md focus:border-indigo-400 focus:outline-none focus:ring focus:ring-indigo-300 focus:ring-opacity-40"
                />
              </div>
              <div className="mt-4">
                <label htmlFor="planCount" className="block text-sm capitalize">
                  계획 뽀모도로 개수
                </label>
                <input
                  id="planCount"
                  name="planCount"
                  type="number"
                  disabled={todoStatus !== 'READY'}
                  value={planCount}
                  onChange={handlePlanCountChange}
                  className={`block w-full px-3 py-2 mt-2 text-gray border border-gray-200 rounded-md focus:border-indigo-400 focus:outline-none focus:ring focus:ring-indigo-300 focus:ring-opacity-40 ${
                    todoStatus !== 'READY' ? 'bg-lightGray' : 'bg-white'
                  }`}
                />
              </div>
              <div className="py-2 text-gray font-thin text-sm">
                계획 뽀모도로는 최대 20개까지 가능합니다 <br />
                이미 시작한 것은 계획 뽀모도로 개수 수정이 불가능합니다
              </div>
              <div className="flex justify-end mt-6 ">
                <button
                  type="submit"
                  className="px-3 py-2 text-sm tracking-wide bg-main-color text-white capitalize transition-colors duration-200 transform bg-indigo-500 rounded-md dark:bg-indigo-600 dark:hover:bg-indigo-700 dark:focus:bg-indigo-700 hover:bg-indigo-600 focus:outline-none focus:bg-indigo-500 focus:ring focus:ring-indigo-300 focus:ring-opacity-50"
                >
                  수정하기
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default TodoDetail;

import { useNavigate } from 'react-router-dom';
import { useTimerStore } from '../../store/timer';
import { useTodosStore } from '../../store/todos';
import { useReviewStore } from '../../store/review';
import resetTimer from '../../utils/resetTimer';

type alert = {
  text: string;
  getIsShow: () => void;
};

const Alert = ({ text, getIsShow }: alert) => {
  const navigate = useNavigate();
  const timer = useTimerStore();
  const todos = useTodosStore();
  const { openReview, closeReview } = useReviewStore();

  const handleClose = () => {
    getIsShow();
  };

  const handleChange = () => {
    getIsShow();

    if (text === '할 일') {
      closeReview();

      timer.showTodo();
      resetTimer(timer, todos, 'reset', todos.todoList);
    } else if (text === '휴식') {
      closeReview();

      timer.showBreak();
      resetTimer(timer, todos, '휴식');
      todos.setTodoId(0);
    } else if (text === 'task') {
      navigate('/tasks');
    } else {
      openReview();
      timer.showReview();
      todos.setTodoId(0);
    }
  };

  return (
    <div>
      <div
        className="fixed inset-0 z-50 overflow-y-auto "
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
            <div className="flex items-center space-x-4 justify-end">
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

            <div className="mt-5">
              <span className="text-main-color">{text} </span>로
              이동하시겠습니까? 진행 중인 타이머가 초기화됩니다.
            </div>

            <div className="flex justify-end mt-6 ">
              <button
                onClick={handleChange}
                className="px-3 py-2 text-sm tracking-wide bg-main-color text-white capitalize transition-colors duration-200 transform bg-indigo-500 rounded-md dark:bg-indigo-600 dark:hover:bg-indigo-700 dark:focus:bg-indigo-700 hover:bg-indigo-600 focus:outline-none focus:bg-indigo-500 focus:ring focus:ring-indigo-300 focus:ring-opacity-50"
              >
                이동하기
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Alert;

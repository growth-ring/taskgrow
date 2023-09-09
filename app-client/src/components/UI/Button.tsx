import { ReactNode } from 'react';

interface ButtonProps {
  children: ReactNode;
  onClick?: () => void;
  showTodo?: boolean;
}

const Button = ({ children, onClick, showTodo }: ButtonProps) => {
  return (
    <button
      className="bg-transparent hover:bg-main-color text-main-color font-semibold hover:text-white py-2 px-4 border border-main-color hover:border-transparent rounded"
      onClick={onClick}
      style={{ background: showTodo ? 'var(--main-color)' : '', color: showTodo ? 'white' : ''}}
    >
      {children}
    </button>
  );
};

export default Button;

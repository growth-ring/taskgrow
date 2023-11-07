import { ReactNode } from 'react';
import { useTimerStore } from '../../store/timer';

interface ButtonProps {
  children: ReactNode;
  onClick: () => void;
  title?: string;
}

const Button = ({ children, onClick, title }: ButtonProps) => {
  const isClick = useTimerStore().selectedBtn === title;

  return (
    <button
      className="bg-transparent hover:bg-main-color text-main-color font-semibold hover:text-white py-2 px-4 border border-main-color hover:border-transparent rounded"
      onClick={onClick}
      style={{
        background: isClick ? 'var(--main-color)' : '',
        color: isClick ? 'white' : '',
      }}
    >
      {children}
    </button>
  );
};

export default Button;

import styled from 'styled-components';
import { useState } from 'react';
import { useReviewStore } from '../../store/review';

const Container = styled.div`
  display: flex;
`;

interface SquareProps {
  active: boolean;
  onClick: () => void;
}

const Square = styled.button<SquareProps>`
  width: 30px;
  height: 30px;
  background-color: ${(props) =>
    props.active ? 'var(--main-color)' : 'var(--sub-yellow-color)'};
  color: ${(props) => (props.active ? 'white' : 'gray')};
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 15px;
`;

const MoodSelect = () => {
  const { setFeelingsScore } = useReviewStore();
  const [activeButtons, setActiveButtons] = useState<boolean[]>(
    Array(10).fill(false),
  );

  const handleClick = (index: number) => {
    setActiveButtons((prevButtons) => prevButtons.map((_, i) => i <= index));
    setFeelingsScore(index + 1);
  };

  return (
    <Container>
      {Array.from({ length: 10 }, (_, index) => (
        <Square
          key={index}
          active={activeButtons[index]}
          onClick={() => handleClick(index)}
        >
          {index + 1}
        </Square>
      ))}
    </Container>
  );
};

export default MoodSelect;

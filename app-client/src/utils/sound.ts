import todoCompleteSound from '../assets/sound/todoComplete.mp3';
import breakCompleteSound from '../assets/sound/breakComplete.mp3';

const todoComplete = () => {
  new Audio(todoCompleteSound).play();
};

const breakComplete = () => {
  new Audio(breakCompleteSound).play();
};

export const sound = (isBreak: boolean) => {
  if (isBreak) {
    breakComplete();
  } else {
    todoComplete();
  }
};

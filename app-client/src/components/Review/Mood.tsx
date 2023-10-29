import styled from 'styled-components';
import MoodSelect from './MoodSelect';
import SelectedMood from './SelectedMood';
import { AiOutlineFrown, AiOutlineSmile } from 'react-icons/ai';

const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 2rem 0;
`;

const MoodSticker = styled.div`
  width: 290px;
  display: flex;
  justify-content: space-between;
  margin-top: 0.5rem;
  font-size: 20px;
  color: gray;
`;

const Mood = () => {
  return (
    <Container>
      <SelectedMood />
      <MoodSelect />
      <MoodSticker>
        <AiOutlineFrown />
        <AiOutlineSmile />
      </MoodSticker>
    </Container>
  );
};

export default Mood;

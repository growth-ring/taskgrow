import styled from 'styled-components';
import MoodSelect from './MoodSelect';
import SelectedMood from './SelectedMood';

const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 2rem 0;
`;

const Mood = () => {
  return (
    <Container>
      <SelectedMood />
      <MoodSelect />
    </Container>
  );
};

export default Mood;

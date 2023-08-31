import styled from 'styled-components';
import Calendar from '../../components/Calendar/Calendar';

import axios from 'axios';

const Main = styled.div`
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
`;

const MainPage = () => {

  return (
    <Main>
      <Calendar />
    </Main>
  );
};

export default MainPage;

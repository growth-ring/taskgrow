import styled from 'styled-components';
import {
  FaRegCalendarCheck,
  FaRegCalendarMinus,
  FaRegCalendarXmark,
} from 'react-icons/fa6';

const Container = styled.div`
  display: flex;
  justify-content: center;
  color: white;
`;

const Stats = styled.div<{ bg: string }>`
  display: flex;
  align-items: center;
  flex-direction: column;
  justify-content: center;
  border-radius: 50%;
  background-color: ${(props) => props.bg};
  width: 40vw;
  height: 10vw;

  @media (max-width: 767px) {
    width: 30vw;
    height: 20vw;
    font-size: 14px;
    margin: 5px 2px;
  }

  @media (min-width: 768px) and (max-width: 1023px) {
    font-size: 16px;
    margin: 10px 5px;
  }

  @media (min-width: 1024px) {
    font-size: 18px;
    margin: 20px 10px;
  }
`;

const StatsIcon = styled.div`
  color: ${(props) => props.color};

  @media (max-width: 767px) {
    width: 20px;
    height: 20px;
    margin-bottom: 7px;
  }

  @media (min-width: 768px) and (max-width: 1023px) {
    width: 25px;
    height: 25px;
    margin-bottom: 10px;
  }

  @media (min-width: 1024px) {
    width: 30px;
    height: 30px;
    margin-bottom: 15px;
  }
`;

const Title = styled.div`
  margin-bottom: 6px;

  @media (max-width: 767px) {
    margin-bottom: 3px;
  }
`;

const Count = styled.div`
  font-weight: bold;
`;

const Circle = () => {
  return (
    <Container>
      <Stats bg="var(--main-color)">
        <StatsIcon as={FaRegCalendarCheck} />
        <Title>완료</Title>
        <Count>1</Count>
      </Stats>
      <Stats bg="var(--sub-blue-color)">
        <StatsIcon as={FaRegCalendarMinus} />
        <Title>진행중</Title>
        <Count>15</Count>
      </Stats>
      <Stats bg="var(--line-color)">
        <StatsIcon as={FaRegCalendarXmark} />
        <Title>미완료</Title>
        <Count>3</Count>
      </Stats>
    </Container>
  );
};

export default Circle;

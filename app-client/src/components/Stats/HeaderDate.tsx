import styled from 'styled-components';
import { useState } from 'react';
import { SlArrowLeft, SlArrowRight } from 'react-icons/sl';

const Container = styled.div`
  display: flex;
  justify-content: space-between;

  @media (max-width: 767px) {
    padding: 0 20px;
    height: 50px;
  }

  @media (min-width: 768px) {
    padding: 0 40px;
    height: 64px;
  }
`;

const DateBox = styled.div`
  display: flex;
  align-items: center;

  @media (max-width: 767px) {
    font-size: 18px;
  }

  @media (min-width: 768px) and (max-width: 1023px) {
    font-size: 20px;
  }

  @media (min-width: 1024px) {
    font-size: 22px;
  }
`;

const Arrow = styled.button`
  @media (max-width: 767px) {
    margin: 0 20px;
  }

  @media (min-width: 768px) and (max-width: 1023px) {
    margin: 0 60px;
  }

  @media (min-width: 1024px) {
    margin: 0 100px;
  }
`;

const Today = styled.div`
  text-align: center;

  @media (max-width: 767px) {
    width: 160px;
  }

  @media (min-width: 768px) and (max-width: 1023px) {
    width: 170px;
  }

  @media (min-width: 1024px) {
    width: 180px;
  }
`;

const HeaderDate = () => {
  const currentDate = new Date();
  const [month, setMonth] = useState<number>(currentDate.getMonth() + 1);
  const [year, setYear] = useState<number>(currentDate.getFullYear());

  const handleShowDate = (clickAction: string) => {
    let newMonth = month;

    if (clickAction === 'previous') {
      --newMonth;
      if (newMonth > 0) {
        setMonth(newMonth);
      } else {
        setMonth(12);
        setYear(year - 1);
      }
    } else {
      ++newMonth;
      if (newMonth < 13) {
        setMonth(newMonth);
      } else {
        setMonth(1);
        setYear(year + 1);
      }
    }
  };
  return (
    <Container>
      <DateBox>
        <Arrow onClick={() => handleShowDate('previous')}>
          <SlArrowLeft />
        </Arrow>
        <Today>
          {year}년 {month}월
        </Today>
        <Arrow onClick={() => handleShowDate('next')}>
          <SlArrowRight />
        </Arrow>
      </DateBox>
    </Container>
  );
};

export default HeaderDate;

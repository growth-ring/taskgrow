import styled from 'styled-components';
import moment from 'moment';
import { useTodosStore } from '../../store/todos';
import { SlArrowLeft, SlArrowRight } from "react-icons/sl";

const Arrow = styled.button`
    margin: 0 100px;
`;

const Today = styled.div`
    width: 180px;
    text-align: center;
`;

const HeaderDate = () => {
  const { today, setToday } = useTodosStore();
  const [years, month, day] = today.split("-");

  const handleShowDate = (date:string) => {
    const todayDate = new Date(today);
    const currentDate = date === 'previous' ? todayDate.getDate() - 1 : todayDate.getDate() + 1;
    setToday(moment(todayDate.setDate(currentDate)).format('YYYY-MM-DD'));
  }

  return (
    <>
      <Arrow onClick={() => handleShowDate('previous')}><SlArrowLeft /></Arrow>
      <Today>{`${years}년 ${month}월 ${day}일`}</Today>
      <Arrow onClick={() => handleShowDate('next')}><SlArrowRight /></Arrow>
    </>
  );
};

export default HeaderDate;

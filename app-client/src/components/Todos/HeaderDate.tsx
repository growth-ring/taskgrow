import styled from 'styled-components';
import moment from 'moment';
import { useTodosStore } from '../../store/todos';
import { SlArrowLeft, SlArrowRight } from "react-icons/sl";

const Arrow = styled.button`
    margin: 0 100px;
`;

const HeaderDate = () => {
  const { today, setToday } = useTodosStore();
  const [years, month, day] = today.split("-");

  const handleShowPrevDate = () => {
    const todayDate = new Date(today);
    const currentDate = todayDate.getDate() - 1;
    setToday(moment(todayDate.setDate(currentDate)).format('YYYY-MM-DD'));
  }

  const handleShowNextDate = () => {
    const todayDate = new Date(today);
    const currentDate = todayDate.getDate() + 1;
    setToday(moment(todayDate.setDate(currentDate)).format('YYYY-MM-DD'));
  }

  return (
    <>
      <Arrow onClick={handleShowPrevDate}><SlArrowLeft /></Arrow>
      <div>{`${years}년 ${month}월 ${day}일`}</div>
      <Arrow onClick={handleShowNextDate}><SlArrowRight /></Arrow>
    </>
  );
};

export default HeaderDate;

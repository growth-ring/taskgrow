import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import moment from 'moment';
import { useParams } from 'react-router-dom';
import { SlArrowLeft, SlArrowRight } from 'react-icons/sl';

const Arrow = styled.button`
  margin: 0 100px;
`;

const Today = styled.div`
  width: 180px;
  text-align: center;
`;

const HeaderDate = () => {
  const { date } = useParams();
  const navigate = useNavigate();

  if (!date) {
    return null;
  }

  const [years, month, day] = date.split('-');

  const handleShowDate = (clickDate: string) => {
    const todayDate = new Date(date);
    const currentDate =
      clickDate === 'previous'
        ? todayDate.getDate() - 1
        : todayDate.getDate() + 1;
    const clickTodayDate = moment(todayDate.setDate(currentDate)).format(
      'YYYY-MM-DD',
    );
    navigate(`/todos/${clickTodayDate}`);
  };

  return (
    <>
      <Arrow onClick={() => handleShowDate('previous')}>
        <SlArrowLeft />
      </Arrow>
      <Today>{`${years}년 ${month}월 ${day}일`}</Today>
      <Arrow onClick={() => handleShowDate('next')}>
        <SlArrowRight />
      </Arrow>
    </>
  );
};

export default HeaderDate;

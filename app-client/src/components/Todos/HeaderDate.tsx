import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import moment from 'moment';
import { useParams } from 'react-router-dom';
import { SlArrowLeft, SlArrowRight } from 'react-icons/sl';
import { useUser } from '../../store/user';
import { useTask } from '../../store/task';
import { getAllTask, clickTask } from '../../utils/checkTaskExists';

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
  const { date } = useParams();
  const navigate = useNavigate();
  const { userId } = useUser();
  const { setSelectedTaskId } = useTask();

  if (!date) {
    return null;
  }

  const [years, month, day] = date.split('-');

  const handleShowDate = async (clickDate: string) => {
    const today = moment(date);
    if (clickDate === 'previous') {
      today.subtract(1, 'day');
    } else {
      today.add(1, 'day');
    }
    const taskDate = today.format('YYYY-MM-DD');

    const startDate = moment(date).subtract(1, 'day').format('YYYY-MM-DD');
    const endDate = moment(date).add(1, 'day').format('YYYY-MM-DD');

    navigate(`/todos/${taskDate}`);
    const monthTaskDate = await getAllTask({ userId, startDate, endDate });
    const taskId = await clickTask({ userId, monthTaskDate, taskDate });
    setSelectedTaskId(taskId);
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

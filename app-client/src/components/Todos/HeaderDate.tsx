import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import moment from 'moment';
import { useParams } from 'react-router-dom';
import { SlArrowLeft, SlArrowRight } from 'react-icons/sl';
import { useUser } from '../../store/user';
import { useTask } from '../../store/task';
import { moveToTask } from '../../utils/checkTaskExists';

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
  const { userId } = useUser();
  const { monthTaskDate, setSelectedTaskId } = useTask();

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
    const userClickDay = moment(todayDate.setDate(currentDate)).format(
      'YYYY-MM-DD',
    );

    const taskId = moveToTask({ userId, monthTaskDate, userClickDay });
    if (typeof taskId === 'number') {
      setSelectedTaskId(taskId);
      navigate(`/todos/${userClickDay}`);
    } else {
      taskId.then((id) => {
        setSelectedTaskId(id);
        navigate(`/todos/${userClickDay}`);
      });
    }
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

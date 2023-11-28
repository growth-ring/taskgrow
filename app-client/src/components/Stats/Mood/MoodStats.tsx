import { useState, useEffect } from 'react';
import styled from 'styled-components';
import Box from '../UI/Box';
import moment from 'moment';
import { getReviewStats } from '../../../services/review';
import { useUser } from '../../../store/user';
import { useDate } from '../../../store/stats';
import { MoodList } from '../../../constants/StatsComment';

const Container = styled.div`
  display: flex;
  justify-content: center;
  text-align: center;

  @media (min-width: 768px) {
    width: 50%;
    flex-direction: column;
  }
`;

const MoodStats = () => {
  const [feelings, setFellings] = useState([]);
  const { userId } = useUser();
  const { year, month } = useDate();

  const feelingsLength = Object.values(feelings).reduce(
    (acc, value) => acc + value,
    0,
  );
  const Title = ['감정', feelingsLength];
  const firstMood = '기분 좋은 날';
  const secondMood = '행복한 날';
  const comment = `${firstMood}이 가장 많았어요`;
  const subComment = `두번째로는 ${secondMood}이 많았어요`;

  const getMoodStats = async () => {
    const firstDay = moment(new Date(year, month - 1, 1)).format('YYYY-MM-DD');
    const lastDay = moment(new Date(year, month, 0)).format('YYYY-MM-DD');

    const userReviewStats = {
      userId: userId.toString(),
      startDate: firstDay,
      endDate: lastDay,
    };

    const feelingsList = await getReviewStats(userReviewStats);
    setFellings(feelingsList);
  };

  const moodFromScore = () => {
    MoodList[5].num = feelings['1'];
    MoodList[4].num = feelings['2'] + feelings['3'];
    MoodList[3].num = feelings['4'] + feelings['5'];
    MoodList[2].num = feelings['6'] + feelings['7'];
    MoodList[1].num = feelings['9'] + feelings['9'];
    MoodList[0].num = feelings['10'];
  };

  useEffect(() => {
    getMoodStats();
    moodFromScore();
  }, []);

  return (
    <Container>
      <Box title={Title} comment={comment} subComment={subComment} />
    </Container>
  );
};

export default MoodStats;

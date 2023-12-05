import { useEffect } from 'react';
import { BarChart, Bar, Tooltip, XAxis, Cell } from 'recharts';
import styled from 'styled-components';
import { useMoods } from '../../../store/mood';

const Container = styled.div`
  display: flex;
  justify-content: center;
  height: 180px;
`;

const CustomTooltip = ({ active, payload }: any) => {
  if (active && payload && payload.length) {
    return (
      <div
        className="custom-tooltip"
        style={{ background: 'var(--sub-blue-color)', color: 'white' }}
      >
        <p className="label">{`${payload[0].payload.name} : ${payload[0].value}`}</p>
      </div>
    );
  }
  return null;
};

const Chart = () => {
  const { moods, topMoods } = useMoods();

  useEffect(() => {
    console.log('topMoods: ', topMoods);
  }, []);

  return (
    <Container>
      <BarChart width={400} height={180} data={moods}>
        <XAxis dataKey="icon" fontSize={22} />
        <Tooltip content={<CustomTooltip />} />
        <Bar dataKey="num">
          {moods.map((entry, index) => (
            <Cell
              key={`cell-${index}`}
              fill={
                entry.num === topMoods.firstMood.num
                  ? `var(--main-color)`
                  : entry.num === topMoods.secondMood.num
                  ? `var(--sub-blue-color)`
                  : `var(--line-color)`
              }
            />
          ))}
        </Bar>
      </BarChart>
    </Container>
  );
};

export default Chart;
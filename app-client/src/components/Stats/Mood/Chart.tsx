import { useState, useEffect } from 'react';
import { BarChart, Bar, Tooltip, XAxis, Cell } from 'recharts';
import styled from 'styled-components';
import { useMoods } from '../../../store/mood';

const Container = styled.div`
  display: flex;
  justify-content: center;
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
  const [chartSize, setChartSize] = useState({
    width: 400,
    height: 180,
    fontSize: 22,
  });

  const handleResize = () => {
    const newWidth = (window.innerWidth / 100) * 40;
    const newHeight = (window.innerHeight / 100) * 20;
    const newFontSize = (window.innerHeight / 100) * 3;
    setChartSize({ width: newWidth, height: newHeight, fontSize: newFontSize });
  };

  useEffect(() => {
    window.addEventListener('resize', handleResize);
    return () => window.removeEventListener('resize', handleResize);
  }, []);

  useEffect(() => {
    console.log('topMoods: ', topMoods);
  }, []);

  return (
    <Container>
      <BarChart width={chartSize.width} height={chartSize.height} data={moods}>
        <XAxis dataKey="icon" fontSize={chartSize.fontSize} />
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

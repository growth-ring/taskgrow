import { useState, useEffect } from 'react';
import { BarChart, Bar, Tooltip, XAxis, Cell } from 'recharts';
import styled from 'styled-components';
import { MoodList } from '../../../constants/StatsComment';

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

  const sortedMoodList = [...MoodList].sort((a, b) => b.num - a.num);

  const firstMood = sortedMoodList[0].num;
  const secondMood = sortedMoodList[1].num;

  useEffect(() => {
    window.addEventListener('resize', handleResize);
    return () => window.removeEventListener('resize', handleResize);
  }, []);

  return (
    <Container>
      <BarChart
        width={chartSize.width}
        height={chartSize.height}
        data={MoodList as { name: string; icon: string; num: number }[]}
      >
        <XAxis dataKey="icon" fontSize={chartSize.fontSize} />
        <Tooltip content={<CustomTooltip />} />
        <Bar dataKey="num">
          {MoodList.map((entry, index) => (
            <Cell
              key={`cell-${index}`}
              fill={
                entry.num === firstMood
                  ? `var(--main-color)`
                  : entry.num === secondMood
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

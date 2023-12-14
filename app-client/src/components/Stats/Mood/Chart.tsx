import { BarChart, Bar, Tooltip, XAxis, Cell } from 'recharts';
import { CircleType } from '../UI/Detail';
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

interface CategoryType {
  entry: {
    icon: string;
    name: string;
    num: number;
  };
}

const Chart = ({ getIsDetail }: CircleType) => {
  const { moods, topMoods, getMoodDetail } = useMoods();

  const handleOnDetail = ({ category }: { category: CategoryType }) => {
    const sub = category.entry.name;
    const CategoryName = `감정(${sub}) 상세보기`;
    getIsDetail({ action: true, category: CategoryName });
    getMoodDetail(sub);
  };

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
              onClick={() => handleOnDetail({ category: { entry } })}
            />
          ))}
        </Bar>
      </BarChart>
    </Container>
  );
};

export default Chart;

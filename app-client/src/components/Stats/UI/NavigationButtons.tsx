import { useState } from 'react';
import styled from 'styled-components';
import {
  IoIosCloseCircle,
  IoIosArrowDropleft,
  IoIosArrowDropright,
} from 'react-icons/io';
import { CircleType } from './Detail';
import { useStats } from '../../../store/stats';
import { useMoods } from '../../../store/mood';
import { useTodosStore } from '../../../store/todos';

const Wapper = styled.div`
  display: flex;
  color: #949494;
  font-size: 19px;
`;

const Page = styled.div`
  display: flex;
  margin-right: 20px;
`;

const PageNum = styled.div`
  margin: 0 20px;
`;

const Btn = styled.button`
  cursor: pointer;
`;

interface NaviationBtnType extends CircleType {
  category: string;
}

const NavigationButtons = ({ getIsDetail, category }: NaviationBtnType) => {
  const [page, setPage] = useState(1);
  const { todosDetail, todosTotal, moodDetail, moodTotal } = useStats();
  const { getMoodDetail } = useMoods();
  const { getTodoDetail } = useTodosStore();

  const handleCloseDetail = () => {
    getIsDetail({ action: false, category: category });
  };

  const handleShowPage = (action: string) => {
    const newPage = action === 'left' ? page - 1 : page + 1;

    if (category === '감정') {
      const isNextPage = moodTotal - (newPage - 1) * 10 > 0;
      if (isNextPage && newPage > 0) {
        setPage(newPage);
        getMoodDetail({ subject: moodDetail, page: newPage });
      }
    } else {
      const isNextPage = todosTotal - (newPage - 1) * 10 > 0;
      if (isNextPage && newPage > 0) {
        setPage(newPage);
        getTodoDetail({ status: todosDetail, page: newPage });
      }
    }
  };

  return (
    <Wapper>
      <Page>
        <Btn onClick={() => handleShowPage('left')}>
          <IoIosArrowDropleft />
        </Btn>
        <PageNum>{page}</PageNum>
        <Btn onClick={() => handleShowPage('right')}>
          <IoIosArrowDropright />
        </Btn>
      </Page>
      <Btn onClick={handleCloseDetail}>
        <IoIosCloseCircle />
      </Btn>
    </Wapper>
  );
};

export default NavigationButtons;

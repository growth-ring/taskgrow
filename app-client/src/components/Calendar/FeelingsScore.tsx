import styled from 'styled-components';

import pc_cry from '../../assets/pc_cry.png';
import pc_sad from '../../assets/pc_sad.png';
import pc_soso from '../../assets/pc_soso.png';
import pc_good from '../../assets/pc_good.png';
import pc_nice from '../../assets/pc_nice.png';
import pc_happy from '../../assets/pc_happy.png';

const FeelingsScore = ({ score }: { score: number }) => {
  let feeling = '';
  let bg = '';

  if (score <= 1) {
    feeling = pc_cry;
    bg = 'red';
  } else if (score <= 3) {
    feeling = pc_sad;
    bg = 'orange';
  } else if (score <= 5) {
    feeling = pc_soso;
    bg = '#fcee1e';
  } else if (score <= 7) {
    feeling = pc_good;
    bg = '#0ba322';
  } else if (score <= 9) {
    feeling = pc_nice;
    bg = '#338bff';
  } else {
    feeling = pc_happy;
    bg = '#89d1fa';
  }

  const Image = styled.img`
    background: ${bg};

    @media (min-width: 768px) and (max-width: 1023px) {
      width: 150px;
      height: 80px;
    }

    @media (min-width: 1024px) {
      width: 160px;
      height: 90px;
    }
  `;

  return <Image src={feeling} />;
};

export default FeelingsScore;

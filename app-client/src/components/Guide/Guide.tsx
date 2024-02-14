import { useState, useEffect } from 'react';
import styled from 'styled-components';
import mobile from '../../assets/guide/mobile.png';
import pc from '../../assets/guide/pc.png';

const Guide = () => {
  const [showGuide, setShowGuide] = useState(true);

  useEffect(() => {
    const isGuideShow = localStorage.getItem('guide');
    if (isGuideShow) {
      setShowGuide(false);
    }
  }, [showGuide]);

  const handleBackDropClick = () => {
    localStorage.setItem('guide', 'true');
    setShowGuide(false);
  };

  return (
    <Backdrop onClick={handleBackDropClick} $showGuide={showGuide}>
      <Image />
    </Backdrop>
  );
};

export default Guide;
const Backdrop = styled.div<{ $showGuide: boolean }>`
  display: ${({ $showGuide }) => ($showGuide ? 'flex' : 'none')};
  position: fixed;
  inset: 0;
`;

const Image = styled.img`
  content: url(${pc});
  @media (max-width: 767px) {
    content: url(${mobile});
  }

  width: 100%;
  height: 100%;
`;

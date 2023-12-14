import styled from 'styled-components';

const Review = styled.input`
  margin: 0 auto;
  background-color: white;
  padding: 1rem;
  border-radius: 1rem;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
  resize: none;
  margin-bottom: 1rem;

  @media (max-width: 1023px) {
    width: 300px;
    height: 30px;
  }

  @media (min-width: 1024px) {
    width: 470px;
    height: 30px;
  }
`;

const ReviewSubject = () => {
  return (
    <>
      <Review type="text" maxLength={50} placeholder="오늘의 한 줄 요약" />
    </>
  );
};

export default ReviewSubject;

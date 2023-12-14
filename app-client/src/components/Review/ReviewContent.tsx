import styled from 'styled-components';

const Review = styled.textarea`
  margin: 0 auto;
  background-color: white;
  padding: 1rem;
  border-radius: 1rem;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
  resize: none;
  margin-bottom: 1rem;

  @media (max-width: 1023px) {
    width: 300px;
    height: 180px;
  }

  @media (min-width: 1024px) {
    width: 470px;
    height: 240px;
  }
`;

interface ContentType {
  content: string;
  getInputContent: (content: string) => void;
}

const ReviewContent = ({ content, getInputContent }: ContentType) => {
  const handleInputChange = (event: React.ChangeEvent<HTMLTextAreaElement>) => {
    getInputContent(event.target.value);
  };

  return (
    <>
      <Review value={content} onChange={handleInputChange}></Review>
    </>
  );
};
export default ReviewContent;

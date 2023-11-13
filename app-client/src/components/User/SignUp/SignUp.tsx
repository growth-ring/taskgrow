import Title from '../Title';
import Form from '../Form';
import Button from '../Button';

const SignUp = () => {
  return (
    <>
      <div>
        <Title title="회원가입" subTitle="이름과 비밀번호를 입력해 주세요" />
        <Form type="signUp" />
      </div>
      <Button type="signUp" />
    </>
  );
};

export default SignUp;

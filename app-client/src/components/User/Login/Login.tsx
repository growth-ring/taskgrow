import Title from '../Title';
import Form from '../Form';
import Button from '../Button';

const Login = () => {
  return (
    <>
      <div>
        <Title title="Task grow" subTitle="Pomodoro & Todo App" />
        <Form type="login" />
      </div>
      <Button type="login" />
    </>
  );
};

export default Login;

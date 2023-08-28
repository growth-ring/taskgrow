import Todo from './Todo';

const TodoList = () => {
  return (
    <div className="max-w-lg mx-auto my-10 bg-white p-8 rounded-xl shadow shadow-slate-300">
      <div id="tasks" style={{ width: "100%", height: "100%"}}>
        <Todo
          title="밥먹기"
          completed={true}
          planCount = '3'
          performCount = '3'
          onClick={() => alert('Task clicked!')}
        />
        <Todo
          title="영단어 외우기"
          completed={false}
          planCount = '2'
          performCount = '1'
          onClick={() => alert('Task clicked!')}
        />
      </div>
    </div>
  );
};

export default TodoList;

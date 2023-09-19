import { rest } from 'msw';
import todos from './dummy.json';

export const handlers = [
  rest.get('/test', async (req, res, ctx) => {
    await sleep(200);
    return res(ctx.status(200), ctx.json(todos));
  }),

  rest.post('/test', async (req, res, ctx) => {
    await sleep(200);
    let todoId = 0;
    const newTodoId = todos.map((value) => {
      if (todoId < value.todo_id) {
        todoId = value.todo_id + 1;
      }
    });
    const newData = { ...req.body, todo_id: todoId, status: 'ready' };
    todos.push(newData);
    return res(ctx.status(201), ctx.json(newData));
  }),

  //   rest.delete('/test', async (req, res, ctx) => {
  //     await sleep(200);
  //     todos.filter((todo) => todo.todo_id !== req.body.todo_id);
  //     return res(ctx.status(204));
  //   }),
];

async function sleep(timeout: number) {
  return new Promise((resolve) => {
    setTimeout(resolve, timeout);
  });
}

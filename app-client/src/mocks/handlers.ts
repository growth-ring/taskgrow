import { rest } from 'msw';
import todos from './dummy.json';

export const handlers = [
  rest.get('/test', async (req, res, ctx) => {
    await sleep(200);
    return res(ctx.status(200), ctx.json(todos));
  }),

  rest.post('/test', async (req, res, ctx) => {
    await sleep(200);
    todos.push(req.body);
    return res(ctx.status(201), ctx.json(todos));
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

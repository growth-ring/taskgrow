export const calculateTime = (count: number) => {
  const hours = Math.floor(count / 60);
  const minutes = count % 60;
  return { hours, minutes };
};

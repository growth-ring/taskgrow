const formatData = (data: string) => {
  const [year, month, day] = data
    .replace(/\. /g, '-')
    .replace('.', '')
    .split('-');
  return `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`;
};

export const startEndDate = (value: string | object) => {
  const today: any = value === 'today' ? new Date() : value;

  const firstDayOfMonth = new Date(today.getFullYear(), today.getMonth(), 1);

  const sevenDaysAgo = new Date(firstDayOfMonth);
  sevenDaysAgo.setDate(firstDayOfMonth.getDate() - 7);

  const lastDayOfMonth = new Date(today.getFullYear(), today.getMonth() + 1, 0);

  const sevenDaysLater = new Date(lastDayOfMonth);
  sevenDaysLater.setDate(lastDayOfMonth.getDate() + 7);

  const startDate = formatData(sevenDaysAgo.toLocaleDateString());
  const endDate = formatData(sevenDaysLater.toLocaleDateString());
  return { startDate, endDate };
};

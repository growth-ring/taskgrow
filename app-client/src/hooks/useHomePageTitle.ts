const useHomePageTitle = (time: number, timerState: string) => {
  const minute = String(Math.floor((time / (1000 * 60)) % 60)).padStart(2, '0');
  const second = String(Math.floor((time / 1000) % 60)).padStart(2, '0');

  document.title =
    timerState === 'RUNNING' ? `${minute} : ${second}` : 'Taskgrow';
};

export default useHomePageTitle;

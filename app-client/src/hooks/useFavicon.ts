const useFavicon = (percent: number, timerState: string) => {
  const selectClock = (percent: number) => {
    if (timerState !== 'RUNNING') {
      return '/favicon.ico';
    } else {
      const totalImages = 12;
      const imageIndex = Math.ceil((percent / 100) * totalImages);
      return `/timer/timer${imageIndex}.ico`;
    }
  };

  const link = document.querySelector("link[rel~='icon']") as HTMLLinkElement;
  if (link) {
    link.href = selectClock(percent);
  }
};

export default useFavicon;

import moment from 'moment';

function isToday(taskDate: string) {
  const today = moment(new Date()).format('YYYY-MM-DD');
  if (today === taskDate) {
    return true;
  } else {
    alert('오늘 날짜만 가능합니다.');
    return false;
  }
}

export default isToday;

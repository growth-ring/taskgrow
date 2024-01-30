import moment from 'moment';

export function isGuest() {
  const userNameId = localStorage.getItem('userNameId');

  const taskDate = moment(new Date()).format('YYYY-MM-DD');
  localStorage.setItem('taskDate', taskDate);

  if (userNameId === null || userNameId === '') {
    return true;
  } else {
    return false;
  }
}

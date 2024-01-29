export function isGuest() {
  const userNameId = localStorage.getItem('userNameId');

  if (userNameId === '') {
    return true;
  } else {
    return false;
  }
}

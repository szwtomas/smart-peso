export function capitalize(str: string): string {
  return str.charAt(0).toUpperCase() + str.slice(1);
}

export function yyyyMMddToddMMyyyy(date: string): string {
  const [yyyy, MM, dd] = date.split('-');
  return `${dd}/${MM}/${yyyy}`;
}

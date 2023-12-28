export function capitalize(str: string): string {
  return str.charAt(0).toUpperCase() + str.slice(1);
}

export function yyyyMMddToddMMyyyy(date: string): string {
  const [yyyy, MM, dd] = date.split('-');
  return `${dd}/${MM}/${yyyy}`;
}

export function roundFloatNumber(num: number): number {
    const numString = String(num);
    const numParts = numString.split('.');
    const integerPart = numParts[0].replace('-', '');

    if (integerPart.length === 1) {
        return Math.round(num * 100) / 100;
    } else if (integerPart.length === 2) {
        return Math.round(num * 10) / 10;
    } else {
        return Math.round(num);
    }
}

export function monthNumberToString(monthNumber: number): string {
  switch (monthNumber) {
    case 0:
      return "Ene";
    case 1:
      return "Feb";
    case 2:
      return "Mar";
    case 3:
      return "Abr";
    case 4:
      return "May";
    case 5:
      return "Jun";
    case 6:
      return "Jul";
    case 7:
      return "Ago";
    case 8:
      return "Sep";
    case 9:
      return "Oct";
    case 10:
      return "Nov";
    case 11:
      return "Dic";
    default:
      throw new Error("Invalid month number");
  }
}

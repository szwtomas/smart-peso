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

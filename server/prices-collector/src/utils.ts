import moment from 'moment-timezone';

export function todayArgentinaTimeZone(): Date {
    return moment().tz("America/Argentina/Buenos_Aires").toDate();
}

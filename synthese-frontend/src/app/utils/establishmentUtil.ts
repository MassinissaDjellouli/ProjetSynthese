export const getOpenDays = (openDays:string[]):string => {
    return openDays.flatMap(day => day.substring(0,3).toUpperCase()).join(", ");
}
export const getOpenHours = (openTime:string,closeTime:string):string => {
    return openTime.replace(":","h") + " - " + closeTime.replace(":","h");
}
export const getPhone = (phone:string) => {
    return "(" + phone.substring(0,3) + ")-" + phone.substring(3,6) + "-" + phone.substring(6,10);
}
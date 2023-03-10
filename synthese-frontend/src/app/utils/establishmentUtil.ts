import { IUser } from '../interfaces/AbstractUser';
import { LoggedInService } from '../services/login/loggedIn/logged-in.service';
import { Admin } from '../interfaces/Admin';
export const getOpenDays = (openDays:string[]):string => {
    return openDays.flatMap(day => day.substring(0,3).toUpperCase()).join(", ");
}
export const getOpenHours = (openTime:string,closeTime:string):string => {
    return openTime.replace(":","h") + " - " + closeTime.replace(":","h");
}
export const getPhone = (phone:string) => {
    return "(" + phone.substring(0,3) + ")-" + phone.substring(3,6) + "-" + phone.substring(6,10);
}
export const getEstablishmentId = <T extends IUser> (loggedInService:LoggedInService) => {
    return (loggedInService.currentLoggedInUser!.userInfo as unknown as T).establishmentId;
  }
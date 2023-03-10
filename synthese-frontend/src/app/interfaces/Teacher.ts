import { IUser } from "./AbstractUser";
import { Course } from "./Course";

export interface Teacher extends IUser{
    id:string;
    firstName: string;
    lastName: string;
    username: string;
    establishmentId: string;
    courses: Course[];
}
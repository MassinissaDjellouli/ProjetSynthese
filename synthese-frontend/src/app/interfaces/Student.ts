import { IUser } from "./AbstractUser";

export interface Student extends IUser{
    id:string;
    firstName: string;
    lastName: string;
    username: string;
    session: number;
}
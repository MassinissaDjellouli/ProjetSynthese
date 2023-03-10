import { IUser } from "./AbstractUser";

export interface Manager extends IUser{
    id:string;    
    firstName: string;
    lastName: string;
    username: string;
}
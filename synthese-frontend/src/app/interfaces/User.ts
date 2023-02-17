import { Student } from './Student';
import { Teacher } from './Teacher';
import { Manager } from './Manager';
import { Admin } from './Admin';
import { Roles } from './Roles';

export interface User{
    username:string;
    password:string;
    userInfo:Student | Teacher | Manager | Admin;
    lastLoginTime:Date;
    role:Roles;
}
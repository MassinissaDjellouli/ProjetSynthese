import { Injectable } from '@angular/core';
import { LoginType } from '../../interfaces/LoginType';
import { isError, parseError, RequestService } from '../request/request.service';
import { ApiError } from '../../interfaces/ApiError';
import { ApiResponse } from '../../interfaces/ApiResponse';
import { Credentials } from 'src/app/interfaces/Credentials';
import { LoggedInService } from './loggedIn/logged-in.service';
import { Roles } from '../../interfaces/Roles';


@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private reqService:RequestService, private loggedInService:LoggedInService){
    this.loginType = "Étudiant"
  }
  error:string = ""
  loginType:LoginType;
  getRole = ():Roles => {
    switch(this.loginType) {
      case "Étudiant":
        return "Student";
      case "Professeur":
        return "Teacher";
      case "Administrateur":
        return "Admin";
      case "Gestionnaire":
        return "Manager";
      default:
        throw new Error("Non-existant user type:" + this.loginType);
    }
  }
  changeLoginType = (newType:LoginType) => { this.loginType = newType }   
  login = async (credentials:Credentials):Promise<boolean> => {
    let loginFunc = this.getLoginFunction();
    let result:ApiError|ApiResponse = await loginFunc(credentials);
    if(isError(result)){
      this.error = parseError(result);
      return false;
    }
    let response = result as ApiResponse;
    this.loggedInService.login({
      username:credentials.username,
      password:credentials.password,
      userInfo:response.data,
      lastLoginTime:new Date(),
      role:this.getRole()
  });
    return true;
  }
  private getLoginFunction = ():Function => {
    switch(this.loginType) {
      case "Étudiant":
        return this.studentLogin;
      case "Professeur":
        return this.teacherLogin;
      case "Administrateur":
        return this.adminLogin;
      case "Gestionnaire":
        return this.managerLogin;
      default:
        throw new Error("Non-existant user type:" + this.loginType);
    }
  }

  private managerLogin = async (credentials:Credentials) => {
    this.reqService.postRequest<Credentials>("manager/login",credentials)
  }

  private adminLogin = async (credentials:Credentials) => {
    let res = await this.reqService.postRequest<Credentials>("admin/login",credentials);
    console.log(res);
    return res
  }

  private studentLogin = async (credentials:Credentials) => {
    this.reqService.postRequest<Credentials>("student/login",credentials)
  }

  private teacherLogin = async (credentials:Credentials) => {
    this.reqService.postRequest<Credentials>("teacher/login",credentials)
  }
}

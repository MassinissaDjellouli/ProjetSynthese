import { Injectable } from '@angular/core';
import { LoginType } from '../../interfaces/LoginType';
import { isError, parseError, RequestService } from '../request/request.service';
import { ApiError } from '../../interfaces/ApiError';
import { ApiResponse } from '../../interfaces/ApiResponse';
import { Credentials } from 'src/app/interfaces/Credentials';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private reqService:RequestService) { 
    this.loginType = "Étudiant"
  }
  error:string = ""
  loginType:LoginType;
  changeLoginType = (newType:LoginType) => { this.loginType = newType }   
  login = async (credentials:Credentials):Promise<boolean> => {
    let loginFunc = this.getLoginFunction();
    let result:ApiError|ApiResponse = await loginFunc(credentials);
    if(isError(result)){
      this.error = parseError(result);
      return false;
    }
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
    this.reqService.getRequest("manager/login")
  }

  private adminLogin = async (credentials:Credentials) => {
    console.log("admin");
    
  }

  private studentLogin = async (credentials:Credentials) => {
    console.log("stud");

  }

  private teacherLogin = async (credentials:Credentials) => {
    console.log("teach");

  }
}

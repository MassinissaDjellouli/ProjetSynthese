import { Injectable } from '@angular/core';
import { LoginType } from '../../interfaces/LoginType';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor() { 
    this.loginType = "Étudiant"
  }

  loginType:LoginType;
  changeLoginType = (newType:LoginType) => { this.loginType = newType }   
  login = async ():Promise<boolean> => {
    let loginFunc = this.getLoginFunction();
    let result = await loginFunc();

    return true;
  }
  private getLoginFunction = () => {
    switch(this.loginType) {
      case "Étudiant":
        return this.studentLogin;
      case "Professeur":
        return this.teacherLogin;
      case "Administrateur":
        return this.adminLogin;
      default:
        throw new Error("Non-existant user type:" + this.loginType);
    }
  }

  private adminLogin = async () => {
    console.log("admin");
    
  }

  private studentLogin = async () => {
    console.log("stud");

  }

  private teacherLogin = async () => {
    console.log("teach");

  }
}

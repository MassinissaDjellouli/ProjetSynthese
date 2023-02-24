import { Injectable } from '@angular/core';
import { LoginType } from '../../interfaces/LoginType';
import { isError, parseError as parseError, RequestService } from '../request/request.service';
import { ApiError } from '../../interfaces/ApiError';
import { ApiResponse } from '../../interfaces/ApiResponse';
import { Credentials } from 'src/app/interfaces/Credentials';
import { LoggedInService } from './loggedIn/logged-in.service';
import { Roles } from '../../interfaces/Roles';
import { Errors } from '../../interfaces/ErrorsEnum';
import { LoadingService } from '../loading/loading.service';
import { Router } from '@angular/router';
import { User } from 'src/app/interfaces/User';
import { Manager } from '../../interfaces/Manager';
import { Student } from 'src/app/interfaces/Student';
import { Teacher } from 'src/app/interfaces/Teacher';


@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private router:Router,private reqService: RequestService, private loggedInService: LoggedInService, private loadingService: LoadingService) {
    this.loginType = "Étudiant"
  }
  error: string = ""
  loginType: LoginType;
  selectMultiple = false;
  userChoices:Teacher[] | Student[] | Manager[] = [];
  multipleUserCommonInfo:any;
  getMultipleUsers = ():typeof this.userChoices => {
    return this.userChoices;
  }
  getRole = (): Roles => {
    switch (this.loginType) {
      case "Étudiant":
        return Roles.Student;
      case "Professeur":
        return Roles.Teacher;
      case "Administrateur":
        return Roles.Admin;
      case "Gestionnaire":
        return Roles.Manager;
      default:
        throw new Error("Non-existant user type:" + this.loginType);
    }
  }
  changeLoginType = (newType: LoginType) => { this.loginType = newType }
  login = async (credentials: Credentials): Promise<boolean> => {
    this.loadingService.startLoading();
    let result = await this.doLogin(credentials);
    this.loadingService.stopLoading();
    return result;
  }

  private doLogin = async (credentials: Credentials) => {
    try {
      
      let loginFunc = this.getLoginFunction();
      let result: ApiError | ApiResponse = await loginFunc(credentials);
      if (isError(result)) {
        this.error = parseError(result);
        return false;
      }
      let response = result as ApiResponse;
      if (response.data.length == undefined) {
        this.loggedInService.login({
          username: credentials.username,
          password: credentials.password,
          userInfo: response.data,
          lastLoginTime: new Date(),
          role: this.getRole()
        });
        this.error = "";
        return true;
      }
      if(response.data.length == 1){
        this.loggedInService.login({
            username: credentials.username,
            password: credentials.password,
            userInfo: response.data[0],
            lastLoginTime: new Date(),
            role: this.getRole()
        })
        this.error = "";
        return true;
      }
      if(response.data.length > 1){
        this.selectMultiple = response.data.length > 1;
        this.userChoices = response.data;
        this.multipleUserCommonInfo = {
          username: credentials.username,
          password: credentials.password,
          lastLoginTime: new Date(),
          role: this.getRole()
      }
        this.router.navigate(["/selectUser"]);
        return true;
      }
      throw new Error("Unknown error");
      } catch(e) {
      console.log(e);
      this.error = "Unknown error";
      return false;
    }
  }
  private getLoginFunction = (): Function => {
    switch (this.loginType) {
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

  private managerLogin = async (credentials: Credentials) => {
    let res = await this.reqService.postRequest<Credentials>("manager/login", credentials)
    return res
  }

  private adminLogin = async (credentials: Credentials) => {
    let res = await this.reqService.postRequest<Credentials>("admin/login", credentials);
    return res
  }

  private studentLogin = async (credentials: Credentials) => {
    let res = await this.reqService.postRequest<Credentials>("student/login", credentials)
    return res
  }

  private teacherLogin = async (credentials: Credentials) => {
    let res = await this.reqService.postRequest<Credentials>("teacher/login", credentials)
    return res
  }
}

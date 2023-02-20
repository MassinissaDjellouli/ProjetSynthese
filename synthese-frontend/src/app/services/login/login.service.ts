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


@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private reqService: RequestService, private loggedInService: LoggedInService, private loadingService: LoadingService) {
    this.loginType = "Étudiant"
  }
  error: string = ""
  loginType: LoginType;
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
        console.log(this.error);
        return false;
      }
      let response = result as ApiResponse;
      this.loggedInService.login({
        username: credentials.username,
        password: credentials.password,
        userInfo: response.data,
        lastLoginTime: new Date(),
        role: this.getRole()
      });
      this.error = "";
      return true;
    } catch {
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

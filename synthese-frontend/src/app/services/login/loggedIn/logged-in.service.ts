import { Injectable } from '@angular/core';
import { User } from 'src/app/interfaces/User';

@Injectable({
  providedIn: 'root'
})
export class LoggedInService {

  currentLoggedInUser:User|undefined;

  constructor() { }

  login = (user:User) => {
    this.currentLoggedInUser = user;
    this.saveLoginToCookie(user);
  }

  loginFromCookie = () => {
    let user = this.getLoginFromCookie();
    if(user == undefined){
      return;
    }
    this.currentLoggedInUser = user;
  }

  getLoginFromCookie = () => {
    let date = new Date();
    let cookie = document.cookie;
    let cookieArray = cookie.split(";");
    let cookieObject:any = {};
    for(let i = 0; i < cookieArray.length; i++){
      let cookieKeyValue = cookieArray[i].split("=");
      cookieObject[cookieKeyValue[0]] = cookieKeyValue[1];
    }
    if(cookieObject.user == undefined || cookieObject.user === "none"){
      this.deleteCookie();
      return undefined;
    }
    if(new Date(cookieObject.expires) < date){
      this.deleteCookie();
      return undefined;
    }
    this.saveLoginToCookie(JSON.parse(cookieObject.user));
    return JSON.parse(cookieObject.user) as User;
  }

  saveLoginToCookie = (user:User) => {
    let date = new Date();
    document.cookie = `user=${JSON.stringify(user)}; expires=${JSON.stringify(date.setDate(date.getDate() + 1))}; path=/; SameSite=Lax; Secure;`;
  }

  deleteCookie = () => {
    document.cookie = "user=none";
  }

  logout = () => {
    this.currentLoggedInUser = undefined;
  }
}

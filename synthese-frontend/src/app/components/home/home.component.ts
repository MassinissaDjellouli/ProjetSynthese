import { Component } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { LoginService } from '../../services/login/login.service';
import { LoginType } from '../../interfaces/LoginType';
import { LoggedInService } from '../../services/login/loggedIn/logged-in.service';
import { Roles } from '../../interfaces/Roles';
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {

  Roles = Roles;
  constructor(private loginServ:LoginService,private loggedInService:LoggedInService){
    this.items = [
      {label: "Étudiant"},
      {label:"Professeur"},
      {label:"Gestionnaire"},
      {label:"Administrateur"}
    ] 
    this.errors = {}
    let tab = loginServ.loginType;
    this.activeItem = this.items.find(item => item.label == tab)!;
    this.items.forEach(item => {
      this.errors[item.label!] = "";
    })
}

  isLoggedIn = () => {
    return this.loggedInService.currentLoggedInUser != undefined;
  }

  currentUserRole = () => {
    if(this.loggedInService.currentLoggedInUser == undefined){
      return undefined;
    }
    return this.loggedInService.currentLoggedInUser.role;
  }
  changeActiveLogin = (item:MenuItem) => {
    this.changeTab(item);
    this.activeItem = item;
    this.loginServ.changeLoginType(item.label as LoginType)
  }
  errors:any;
  private changeTab = (item:MenuItem) => {
    this.errors[this.activeItem.label!] = this.loginServ.error;
    this.loginServ.error = this.errors[item.label!];
  }
  items:MenuItem[]
  activeItem:MenuItem;
}

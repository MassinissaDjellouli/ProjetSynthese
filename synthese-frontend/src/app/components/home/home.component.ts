import { Component } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { LoginService } from '../../services/login/login.service';
import { LoginType } from '../../interfaces/LoginType';
import { LoggedInService } from '../../services/login/loggedIn/logged-in.service';
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {

  constructor(private loginServ:LoginService,private loggedInService:LoggedInService){

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
    this.loginServ.changeLoginType(item.label as LoginType)
  }

  items:MenuItem[] = [
    {label: "Étudiant"},
    {label:"Professeur"},
    {label:"Gestionnaire"},
    {label:"Administrateur"}
  ] 
  activeItem:MenuItem = this.items[0];
}

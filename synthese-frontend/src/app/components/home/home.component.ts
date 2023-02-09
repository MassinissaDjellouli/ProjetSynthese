import { Component } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { LoginService } from '../../services/login/login.service';
import { LoginType } from '../../interfaces/LoginType';
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {

  constructor(private loginServ:LoginService){

  }

  changeActiveLogin = (item:MenuItem) => {
    this.loginServ.changeLoginType(item.label as LoginType)
  }

  items:MenuItem[] = [
    {label: "Étudiant"},
    {label:"Professeur"},
    {label:"Administrateur"}
  ] 
  activeItem:MenuItem = this.items[0];
}

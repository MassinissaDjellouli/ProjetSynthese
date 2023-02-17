import { Component } from '@angular/core';
import { LoggedInService } from '../../services/login/loggedIn/logged-in.service';

@Component({
  selector: 'navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  constructor(private loggedInService:LoggedInService) { }
  isLoggedIn = () => {
    return this.loggedInService.currentLoggedInUser != undefined;
  }
  logout = () => {
    this.loggedInService.logout();
  }

}

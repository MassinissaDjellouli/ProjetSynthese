import { Component } from '@angular/core';
import { LoggedInService } from '../../services/login/loggedIn/logged-in.service';
import { Manager } from 'src/app/interfaces/Manager';

@Component({
  selector: 'app-manager',
  templateUrl: './manager.component.html',
  styleUrls: ['./manager.component.css']
})
export class ManagerComponent {
  currentUser:Manager;
  constructor(loggedInUser:LoggedInService) {
    this.currentUser = loggedInUser.currentLoggedInUser?.userInfo as Manager;
   }
}

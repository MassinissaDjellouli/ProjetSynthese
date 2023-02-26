import { AfterViewChecked, AfterViewInit, Component } from '@angular/core';
import { Admin } from '../../interfaces/Admin';
import { EstablishmentService } from '../../services/establishment/establishment.service';
import { LoggedInService } from '../../services/login/loggedIn/logged-in.service';
import { Establishment } from '../../interfaces/Establishment';
import { getOpenDays, getOpenHours, getPhone } from 'src/app/utils/establishmentUtil';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent  {
  user:Admin;
  constructor(
    private loggedInService:LoggedInService,
    private establishmentService:EstablishmentService) { 
      this.user = this.loggedInService.currentLoggedInUser!.userInfo as Admin;
      if(this.user == undefined){
        this.loggedInService.logout();
      }
      this.establishmentService.setEstablishments(this.user.id);
    }
  getEstablishments = this.establishmentService.getEstablishments;
  hasEstablishments = () => {
    return this.establishmentService.getEstablishments().length > 0;
  }
  getOpenHours = getOpenHours
  getOpenDays = getOpenDays
  getPhone = getPhone

}



import { AfterViewChecked, AfterViewInit, Component } from '@angular/core';
import { Admin } from '../../interfaces/Admin';
import { EstablishmentService } from '../../services/establishment/establishment.service';
import { LoggedInService } from '../../services/login/loggedIn/logged-in.service';
import { Establishment } from '../../interfaces/Establishment';

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
  getOpenDays = (openDays:string[]):string => {
    return openDays.flatMap(day => day.substring(0,3).toUpperCase()).join(", ");
  }
  getOpenHours = (openTime:string,closeTime:string):string => {
    return openTime.replace(":","h") + " - " + closeTime.replace(":","h");
  }
  getPhone = (phone:string) => {
    return "(" + phone.substring(0,3) + ")-" + phone.substring(3,6) + "-" + phone.substring(6,10);
  }
}



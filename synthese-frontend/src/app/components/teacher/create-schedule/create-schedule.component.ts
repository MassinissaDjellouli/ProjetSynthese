import { Component } from '@angular/core';
import { CalendarOptions } from '@fullcalendar/core';
import timeGridPlugin from '@fullcalendar/timegrid';
import { Establishment } from '../../../interfaces/Establishment';
import { LoggedInService } from '../../../services/login/loggedIn/logged-in.service';
import { getEstablishmentId } from '../../../utils/establishmentUtil';
import { RequestService, isError } from '../../../services/request/request.service';
import { ApiResponse } from 'src/app/interfaces/ApiResponse';
import { LoadingService } from '../../../services/loading/loading.service';
@Component({
  selector: 'app-create-schedule',
  templateUrl: './create-schedule.component.html',
  styleUrls: ['./create-schedule.component.css']
})
export class CreateScheduleComponent {
  calendarEnabled = false;
  WEEK_DAYS = ['Dimanche','Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi', 'Samedi'];
  filteredWeekDays:string[] = [];
  daysPerWeek = () => {console.log(this.filteredWeekDays.length);this.filteredWeekDays.length}
  
  establishment:Establishment|undefined;
  calendarError:string = "";
  calendarOptions: CalendarOptions = {
    plugins: [ timeGridPlugin ],
    initialView: 'timeGridWeek',
    dayHeaderContent: (date) => {
      return this.WEEK_DAYS[date.date.getDay()];
    }
  };
    constructor(loggedInService:LoggedInService,private requestService:RequestService,private loadingService:LoadingService) { 
      this.setEstablishment(getEstablishmentId(loggedInService));
    }
    setEstablishment = async (id: string) => {
      this.loadingService.startLoading();
      let res = await this.requestService.getRequest("manager/establishment/" + id);
      if (isError(res)) {
        this.calendarError = "Impossible de récupérer les données de l'établissement";
        this.loadingService.stopLoading();
        return;
      }
      this.establishment = (res as ApiResponse).data as Establishment;
      this.calendarEnabled = true;
      this.filteredWeekDays = this.establishment.daysPerWeek
      this.loadingService.stopLoading();
    }
}

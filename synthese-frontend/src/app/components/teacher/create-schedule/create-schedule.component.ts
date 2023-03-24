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
  filteredWeekDays:number[] = [];
  setFilteredWeekDays = (daysPerWeek:string[]) => {
    let index = 0;
    for(let i = 0; i < this.WEEK_DAYS.length; i++){
      if(this.WEEK_DAYS[i] == daysPerWeek[index]){
        this.filteredWeekDays.push(i);
        index++;
      }
    }
    this.updateCalendarOptions();
  }
  
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
      this.setFilteredWeekDays(this.establishment.daysPerWeek);
      this.loadingService.stopLoading();
    }
    updateCalendarOptions = () => {
      this.calendarOptions = {
        locale:'fr',
        plugins: [ timeGridPlugin ],
        nowIndicator:true,
        slotMinTime:this.establishment!.openTime,
        slotMaxTime:this.establishment!.closeTime,
        initialView: 'timeGridWeek',
        businessHours:{
          daysOfWeek:this.filteredWeekDays,
          startTime:this.establishment!.classesStartTime,
          endTime:this.getEndTime()
        },
        hiddenDays:this.getHiddenDays(),
        dayHeaderContent: (date) => {
          return this.WEEK_DAYS[date.date.getDay()];
        }
      }
      this.calendarEnabled = true;
    }
    getEndTime = () =>{
      let AMOUNT_OF_PERIODS_WITHOUT_PAUSES = 2
      let MINUTES_PER_HOURS = 60
      let endTime:number[] = this.establishment!.classesStartTime.split(":").map(time => Number.parseInt(time));
      let pauseTime:number = this.establishment!.betweenPeriodsLength;
      let dinnerTime:number = this.establishment!.dinnerLength;
      let periodsPerDay:number = this.establishment!.periodsPerDay;
      let periodLength:number = this.establishment!.periodLength;
      let daysLength:number = dinnerTime * 1 + (periodLength * periodsPerDay) + (pauseTime * (periodsPerDay - AMOUNT_OF_PERIODS_WITHOUT_PAUSES));
      let minutes = daysLength % MINUTES_PER_HOURS;
      let hours = (daysLength - minutes) / MINUTES_PER_HOURS;
      endTime[0] += hours;
      endTime[1] += minutes ;
      if(endTime[1] >= 60){
        endTime[1] -= 60
        endTime[0]++
      }
      return endTime.map(time => {
        return time.toString().length == 2? time.toString() : "0" + time.toString()
      }).join(":");
    }
    getHiddenDays = () => {
      let hiddenDays = [];
      for(let i = 0; i < this.WEEK_DAYS.length; i++){
        if(!this.filteredWeekDays.includes(i)){
          hiddenDays.push(i);
        }
      }
      return hiddenDays
    }
}

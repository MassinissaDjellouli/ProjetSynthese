import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { ApiError } from 'src/app/interfaces/ApiError';
import { Establishment } from 'src/app/interfaces/Establishment';
import { EstablishmentService } from 'src/app/services/establishment/establishment.service';
import { LoggedInService } from 'src/app/services/login/loggedIn/logged-in.service';
import { isError, parseError, RequestService } from 'src/app/services/request/request.service';
import { getOpenDays, getOpenHours, getPhone } from 'src/app/utils/establishmentUtil';

@Component({
  selector: 'app-modify',
  templateUrl: './modify.component.html',
  styleUrls: ['./modify.component.css']
})
export class ModifyComponent {
  establishment:Establishment | undefined;
  modifiedEstablishment:Map<string,any> | undefined;
  getModifiedFields = () => {
    let map = new Map();
    Object.keys(this.establishment!).forEach(element => {
      map.set(element,false)
    });
    return map
  }
  modifiedFields:Map<string,boolean> | undefined;
  constructor(private router:Router,route:ActivatedRoute,
    private loggedInService:LoggedInService, private messageService:MessageService,
    private establishmentService:EstablishmentService, private requestService:RequestService){
    route.params.subscribe((params) => {
      if(params['id'] == undefined){
        router.navigate(['/'])
        return
      }
      let id = params['id'] 
      this.establishment = establishmentService.establishments.find(establishment => establishment.id = id)
      if(this.establishment == undefined){
        router.navigate(['/'])
        return  
      }
      this.modifiedEstablishment = new Map(Object.entries(this.establishment!))
      this.modifiedFields = this.getModifiedFields()
    })
  }

  modifyField = (field:string ,value:any) => {
    this.modifiedEstablishment!.set(field,value)
  }
  getModifiedEstablishment = ():Establishment => {
    return Object.fromEntries(this.modifiedEstablishment!) as Establishment
  }
  isModified = () => {
    return JSON.stringify(this.getModifiedEstablishment()) != JSON.stringify(this.establishment)
  }
  modify = async () => {
    let res = await this.requestService.putRequest<Establishment>("updateEstablishment",this.getModifiedEstablishment())
    if(isError(res)){
      this.sendError(parseError(res as ApiError));
      return;
    }
    this.establishmentService.setEstablishments(this.loggedInService.currentLoggedInUser!.userInfo.id)
    this.router.navigate(['/'])
    
  }
  getOpenTime = getOpenHours
  getOpenDays = getOpenDays
  getPhone = getPhone
  getClassStartTime = (classesStartTime:string) => {
    return classesStartTime.replace(':','h')
  }

  sendError(error:string) {
    this.messageService.add({severity:'error', summary:'Error', detail:error, life: 5000});
  }
}

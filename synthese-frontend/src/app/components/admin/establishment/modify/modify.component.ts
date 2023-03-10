import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { ApiError } from 'src/app/interfaces/ApiError';
import { Establishment } from 'src/app/interfaces/Establishment';
import { Program } from 'src/app/interfaces/Program';
import { EstablishmentService } from 'src/app/services/establishment/establishment.service';
import { LoggedInService } from 'src/app/services/login/loggedIn/logged-in.service';
import { RequestService, isError, parseError } from 'src/app/services/request/request.service';
import { getOpenDays, getOpenHours, getPhone } from 'src/app/utils/establishmentUtil';
import { setupXMLReader } from '../../../../utils/xmlUtil';
import { Observable } from 'rxjs';
import { LoadingService } from '../../../../services/loading/loading.service';

@Component({
  selector: 'app-modify',
  templateUrl: './modify.component.html',
  styleUrls: ['./modify.component.css']
})
export class ModifyComponent {
  uploadedFiles: any[] = []
  programList: Program[] = [];
  programListError: string = '';
  disabled = false;
  establishment:Establishment | undefined;
  modifiedEstablishment:Map<string,any> | undefined;
  sendProgramObs:Observable<boolean> = new Observable<boolean>(() => {
    let interval = setInterval(async () => {
      if(this.establishment == undefined){
        return 
      }
      clearInterval(interval)
      let res = await this.requestService
        .postRequest<Program[]>('admin/establishment/' + this.establishment.id + '/addProgramList',this.programList)
      if(isError(res)){
        this.programUploadErrorHandler(parseError(res as ApiError, "Erreur lors de l'envoi des programmes"))
        this.loadingService.stopLoading()
        this.disabled = false
        return
      }
      this.loadingService.stopLoading()
      this.messageService.add({severity:'success', summary:'Succès', detail:'Programmes envoyés avec succès'})
    },400)
  })
  
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
    private establishmentService:EstablishmentService, private requestService:RequestService,
    private loadingService:LoadingService) {
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
  upload = (event: any) => {
    let file = event.files[0]
    try {
      this.loadingService.startLoading()
        this.sendProgramObs.subscribe()
      this.setProgramListFromFile(file)
    } catch (error) {
      this.programUploadErrorHandler(error)
      return;
    }
  }
  setProgramListFromFile = (file: any) => {
    if ((file.name as string).endsWith('.json')) {
      this.setProgramListFromJson(file)
      return
    }
    if ((file.name as string).endsWith('.xml')) {
      this.setProgramListFromXML(file)
      return
    }
  }
  setProgramListFromJson = (file: any) => {
    let reader: FileReader = new FileReader()
    reader.onload = (event: any) => {
      try {
        let result = JSON.parse(event.target.result).programs as Program[]
        this.setProgramList(result)
      } catch (error) {
        this.programUploadErrorHandler(error)
        return;
      }
    }
    reader.readAsText(file)
  }
  
  setProgramListFromXML = async (file: any) => {
    let reader:FileReader = setupXMLReader("programs")
    reader.readAsText(file)
    reader.onloadend = (e) => {
      try {
        console.log(e);
        
        if (this.programList == undefined || this.programList.length == 0) {
          console.log("error");
          this.programUploadErrorHandler()
          return;
        }
      } catch (error) {
        this.programUploadErrorHandler(error)
        return;
      }
    }
  }

  setProgramList = (programList: Program[]) => {
    if(programList == undefined || programList.length == 0){
      this.programUploadErrorHandler()
      return
    }
    this.programList = programList
    this.programListError = ''
    this.disabled = true
  }
  programUploadErrorHandler = (error:any = 'Invalid file format') => {
    console.log(error);
    this.programListError = error
  }
  isDisabled = () => this.disabled
}

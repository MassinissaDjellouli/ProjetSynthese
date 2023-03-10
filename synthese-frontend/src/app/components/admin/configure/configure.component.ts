import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiResponse } from 'src/app/interfaces/ApiResponse';
import { Program } from 'src/app/interfaces/Program';
import { setupXMLReader } from 'src/app/utils/xmlUtil';
import { ApiError } from '../../../interfaces/ApiError';
import { Establishment } from '../../../interfaces/Establishment';
import { LoadingService } from '../../../services/loading/loading.service';
import { LoggedInService } from '../../../services/login/loggedIn/logged-in.service';
import { RequestService, isError, parseError } from '../../../services/request/request.service';

@Component({
  selector: 'app-configure',
  templateUrl: './configure.component.html',
  styleUrls: ['./configure.component.css']
})

export class ConfigureComponent {
  error = "";
  configuration: FormGroup = new FormGroup({
    name: new FormControl('',Validators.required),
    address: new FormControl('',Validators.required),
    phone: new FormControl('',[Validators.required,Validators.minLength(10),Validators.maxLength(10)]),
    sessionDuration: new FormControl(6,[Validators.required,Validators.min(1),Validators.max(12)]),
    openTime: new FormControl('',[Validators.required]),
    closeTime: new FormControl('',Validators.required),
    classStart: new FormControl('',Validators.required),
    dinnerTime: new FormControl(60,[Validators.required,Validators.min(1),Validators.max(999)]),
    periodsLength: new FormControl(60,[Validators.required,Validators.min(1),Validators.max(999)]),
    periodsPerDay: new FormControl(1,[Validators.required,Validators.min(1),Validators.max(999)]),
    periodsBeforeDinner: new FormControl(1,[Validators.required,Validators.min(1),Validators.max(999)]),
    pauseTime: new FormControl(5,[Validators.required,Validators.min(1),Validators.max(999)])
  })
  requestError: string = "";

  constructor(private router:Router,private loadingService:LoadingService,private requestService:RequestService, private loggedInService:LoggedInService) { }

  uploadedFiles: any[] = []
  programList: Program[] = [];
  programListError: string = '';
  disabled = false;
  selectedDays:string[] = [];
  DAYS_OF_THE_WEEK = [
    { name: 'Dimanche' },
    { name: 'Lundi' },
    { name: 'Mardi' },
    { name: 'Mercredi' },
    { name: 'Jeudi' },
    { name: 'Vendredi' },
    { name: 'Samedi' }
  ]
  
  isDisabled = () => {
    return this.disabled
  }
  upload = (event: any) => {
    let file = event.files[0]
    try {
      if ((file.name as string).endsWith('.json')) {
        this.setProgramListFromJson(file)
        return
      }
      if ((file.name as string).endsWith('.xml')) {
        this.setProgramListFromXML(file)
        return
      }
    } catch (error) {
      this.invalidFileFormat(error)
      return;
    }
  }
  setProgramListFromJson = (file: any) => {
    let reader: FileReader = new FileReader()
    reader.onload = (event: any) => {
      try {
        let result = JSON.parse(event.target.result).programs as Program[]
        this.setProgramList(result)
      } catch (error) {
        this.invalidFileFormat(error)
        return;
      }
    }
    reader.readAsText(file)
  }
  
  setProgramListFromXML = async (file: any) => {
    let reader:FileReader = setupXMLReader("programs")
    reader.readAsText(file)
    reader.onloadend = () => {
      try {
        
        if (this.programList == undefined || this.programList.length == 0) {
          this.invalidFileFormat()
          return;
        }
      } catch (error) {
        this.invalidFileFormat(error)
        return;
      }
    }
  }

  setProgramList = (programList: Program[]) => {
    if(programList == undefined || programList.length == 0){
      this.invalidFileFormat()
      return
    }
    this.programList = programList
    this.programListError = ''
    this.disabled = true
  }
  invalidFileFormat = (error:any = 'Invalid file format') => {
    console.log(error);
    this.programListError = error
  }

  isValid = () => {
    let openTime:Date = new Date();
    let closeTime:Date = new Date();
    let classStartTime:Date = new Date();
    let openTimeConf = this.configuration.get('openTime')?.value.split(":");
    let closeTimeConf = this.configuration.get('closeTime')?.value.split(":");
    let classTimeConf = this.configuration.get('classStart')?.value.split(":");
    
    let periodLength = parseFloat(this.configuration.get('periodsLength')?.value) / 60
    let periodsPerDay = this.configuration.get('periodsPerDay')?.value;
    let periodsBeforeDinner = parseInt(this.configuration.get('periodsBeforeDinner')?.value);
    
    openTime.setHours(parseInt(openTimeConf[0]), parseInt(openTimeConf[1]))
    closeTime.setHours(parseInt(closeTimeConf[0]), parseInt(closeTimeConf[1]))
    classStartTime.setHours(parseInt(classTimeConf[0]), parseInt(classTimeConf[1]))

    let openHours = (closeTime.getHours() + closeTime.getMinutes() / 60) - (openTime.getHours() + openTime.getMinutes() / 60) 
    let enoughHoursForPeriods = openHours >= periodLength * periodsPerDay
    let validHours = openTime < closeTime && classStartTime < closeTime && classStartTime > openTime
    let enoughPeriodsBeforeDinner = periodsBeforeDinner < periodsPerDay
    if(!validHours){
      this.error = "Les heures d'ouverture et de fermeture ne sont pas valides"
      return false
    } 
    if(!enoughHoursForPeriods){
      this.error = "Trop de periodes pour les heures d'ouvertures"
      return false
    } 
    if(!enoughPeriodsBeforeDinner){
      console.log(this.error);
      this.error = "Trop de periodes avant le dinner"
      return false
    } 
    this.error = ''
    return this.configuration.valid && this.selectedDays.length > 0 && validHours && enoughHoursForPeriods && enoughPeriodsBeforeDinner
  }
  save = async () => {
    this.loadingService.startLoading()
    let establishment:Establishment = {
      id:undefined,
      name: this.configuration.get('name')?.value,
      address: this.configuration.get('address')?.value,
      phone: this.configuration.get('phone')?.value,
      sessionLength: this.configuration.get('sessionDuration')?.value,
      openTime: this.configuration.get('openTime')?.value,
      closeTime: this.configuration.get('closeTime')?.value,
      classesStartTime: this.configuration.get('classStart')?.value,
      dinnerLength: this.configuration.get('dinnerTime')?.value,
      periodsPerDay: this.configuration.get('periodsPerDay')?.value,
      periodsBeforeDinner: this.configuration.get('periodsBeforeDinner')?.value,
      betweenPeriodsLength: this.configuration.get('pauseTime')?.value,
      periodLength: this.configuration.get('periodsLength')?.value,
      daysPerWeek: this.selectedDays,
      programs: this.programList,
      adminId: this.loggedInService.currentLoggedInUser!.userInfo.id,
      managers: [],
      students: [],
      teachers: [],
    }
    
    let response = await this.sendEstablishment(establishment)
    if(response == undefined){
      return;
    }
    if(this.programListError != '' && this.programList != undefined && this.programList.length > 0){
      await this.sendPrograms(this.programList, response)
    }
    this.loadingService.stopLoading()
    this.router.navigate(['/'])
  }
  sendEstablishment = async (establishment: Establishment):Promise<string | undefined> => {
    let result = await this.requestService.postRequest<Establishment>('admin/configureEstablishment', establishment);
    if(isError(result)){
      this.requestError = parseError(result as ApiError);
      return undefined;
    }
    return (result as ApiResponse).data as string;
  }

  sendPrograms = async (programs: Program[], establishmentId: string) => {
    let result = await this.requestService.postRequest<Program[]>('admin/addProgramListToEstablishment/' + establishmentId, programs);
    if(isError(result)){
      this.requestError = parseError(result as ApiError);
      return;
    }
  }
}

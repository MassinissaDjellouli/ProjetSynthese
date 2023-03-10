import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { Establishment } from 'src/app/interfaces/Establishment';
import { RequestService, isError, parseError } from '../../../services/request/request.service';
import { Course } from 'src/app/interfaces/Course';
import { ApiError } from 'src/app/interfaces/ApiError';
import { LoadingService } from '../../../services/loading/loading.service';
import { MessageService } from 'primeng/api';
import { setupXMLReader } from 'src/app/utils/xmlUtil';
import { Program } from 'src/app/interfaces/Program';
import { ApiResponse } from 'src/app/interfaces/ApiResponse';
import { getEstablishmentId } from 'src/app/utils/establishmentUtil';
import { LoggedInService } from '../../../services/login/loggedIn/logged-in.service';

@Component({
  selector: 'app-create-classes',
  templateUrl: './create-classes.component.html',
  styleUrls: ['./create-classes.component.css']
})
export class CreateClassesComponent {
  constructor(private requestService: RequestService,private loadingService:LoadingService,
    private messageService:MessageService, private loggedInService:LoggedInService) { 
      this.setPrograms()
    }
  selectedProgram: Program | undefined;
  uploadedFiles: any[] = []
  courseList: Course[] = [];
  disabled: boolean = false;
  courseListError: string = '';
  programs: Program[] = [];
  sendCourseObs:Observable<boolean> = new Observable<boolean>(() => {
    let interval = setInterval(async () => {
      if(this.selectedProgram == undefined){
        return 
      }
      clearInterval(interval)
      let res = await this.requestService
        .postRequest<Course[]>('manager/program/' + this.selectedProgram.id + '/addCourseList',this.courseList)
      if(isError(res)){
        this.courseUploadErrorHandler(parseError(res as ApiError, "Erreur lors de l'envoi des coursemes"))
        this.loadingService.stopLoading()
        this.disabled = false
        return
      }
      this.loadingService.stopLoading()
      this.messageService.add({severity:'success', summary:'Succès', detail:'Coursemes envoyés avec succès'})
    },400)
  })
  isDisabled = () => this.disabled
  upload = (event: any) => {
    let file = event.files[0]
    try {
      this.loadingService.startLoading()
        this.sendCourseObs.subscribe()
      this.setCourseListFromFile(file)
    } catch (error) {
      this.courseUploadErrorHandler(error)
      return;
    }
  }
  setCourseListFromFile = (file: any) => {
    if ((file.name as string).endsWith('.json')) {
      this.setCourseListFromJson(file)
      return
    }
    if ((file.name as string).endsWith('.xml')) {
      this.setCourseListFromXML(file)
      return
    }
  }

  setCourseListFromJson = (file: any) => {
    let reader: FileReader = new FileReader()
    reader.onload = (event: any) => {
      try {
        let result = JSON.parse(event.target.result).courses as Course[]
        this.setCourseList(result)
      } catch (error) {
        this.courseUploadErrorHandler(error)
        return;
      }
    }
    reader.readAsText(file)
  }
  
  setCourseListFromXML = async (file: any) => {
    let reader:FileReader = setupXMLReader("courses")
    reader.readAsText(file)
    reader.onloadend = () => {
      try {
        
        if (this.courseList == undefined || this.courseList.length == 0) {
          this.courseUploadErrorHandler()
          return;
        }
      } catch (error) {
        this.courseUploadErrorHandler(error)
        return;
      }
    }
  }
  setCourseList = (courseList: Course[]) => {
    if(courseList == undefined || courseList.length == 0){
      this.courseUploadErrorHandler()
      return
    }
    this.courseList = courseList
    this.courseListError = ''
    this.disabled = true
  }
  courseUploadErrorHandler = (error:any = 'Invalid file format') => {
    console.log(error);
    this.courseListError = error
  }
  programSelected = () => this.selectedProgram != undefined
  selectProgram = (program:Program) => {
    this.selectedProgram = program
  }

  setPrograms = async () => {
    this.loadingService.startLoading();
    this.programs = [];
    let res = await this.requestService.getRequest("manager/establishment/" + getEstablishmentId(this.loggedInService) + "/programs")
    if (isError(res)) {
      this.loadingService.stopLoading();
      return;
    }
    this.programs = (res as ApiResponse).data as Program[];
    this.loadingService.stopLoading();
  }
  getPrograms = () => this.programs;

  hasPrograms = () => this.programs.length > 0
}

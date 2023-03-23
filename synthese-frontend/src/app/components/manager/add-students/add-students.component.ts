import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MessageService } from 'primeng/api';
import { Observable } from 'rxjs';
import { ApiError } from 'src/app/interfaces/ApiError';
import { LoadingService } from 'src/app/services/loading/loading.service';
import { RequestService, isError, parseError } from 'src/app/services/request/request.service';
import { getEstablishmentId } from '../../../utils/establishmentUtil';
import { LoggedInService } from '../../../services/login/loggedIn/logged-in.service';
import { Manager } from '../../../interfaces/Manager';

@Component({
  selector: 'app-add-students',
  templateUrl: './add-students.component.html',
  styleUrls: ['./add-students.component.css']
})
export class AddStudentsComponent {
  uploadedFiles: any[] = []
  studentList: any[] = [];
  studentListError: string = '';
  disabled = false;
  modifiedEstablishment:Map<string,any> | undefined;
  sendStudentObs:Observable<boolean> = new Observable<boolean>(() => {
    let interval = setInterval(async () => {
      if(getEstablishmentId(this.loggedInService)  == undefined){
        return 
      }
      clearInterval(interval)
      let res = await this.requestService
        .putRequest<any[]>('manager/establishment/' + getEstablishmentId(this.loggedInService) + '/addStudentList',this.studentList)
      if(isError(res)){
        this.studentUploadErrorHandler(parseError(res as ApiError, "Erreur lors de l'envoi des studentmes"))
        this.loadingService.stopLoading()
        this.disabled = false
        return
      }
      this.loadingService.stopLoading()
      this.messageService.add({severity:'success', summary:'Succès', detail:'Liste envoyé avec succès'})
    },400)
  })
  
  constructor(
    private messageService:MessageService,
    private requestService:RequestService,
    private loadingService:LoadingService,
    private loggedInService:LoggedInService) {
  }

  sendError(error:string) {
    this.messageService.add({severity:'error', summary:'Error', detail:error, life: 5000});
  }

  upload = (event: any) => {
    let file = event.files[0]
    try {
      this.loadingService.startLoading()
        this.sendStudentObs.subscribe()
      this.setStudentListFromFile(file)
    } catch (error) {
      this.studentUploadErrorHandler(error)
      return;
    }
  }
  setStudentListFromFile = (file: any) => {
    if ((file.name as string).endsWith('.json')) {
      this.setStudentListFromJson(file)
      return
    }
  }
  setStudentListFromJson = (file: any) => {
    let reader: FileReader = new FileReader()
    reader.onload = (event: any) => {
      try {
        let result = JSON.parse(event.target.result).students as any[]
        this.setStudentList(result)
      } catch (error) {
        this.studentUploadErrorHandler(error)
        return;
      }
    }
    reader.readAsText(file)
  }

  setStudentList = (studentList: any[]) => {
    if(studentList == undefined || studentList.length == 0){
      this.studentUploadErrorHandler()
      return
    }
    this.studentList = studentList
    this.studentListError = ''
    this.disabled = true
  }
  studentUploadErrorHandler = (error:any = 'Invalid file format') => {
    console.log(error);
    this.studentListError = error
  }
  isDisabled = () => this.disabled
}

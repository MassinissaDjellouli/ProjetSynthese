import { Component, Input } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { ApiResponse } from 'src/app/interfaces/ApiResponse';
import { Establishment } from 'src/app/interfaces/Establishment';
import { Teacher } from 'src/app/interfaces/Teacher';
import { EstablishmentService } from 'src/app/services/establishment/establishment.service';
import { RequestService, isError, parseError } from 'src/app/services/request/request.service';

@Component({
  selector: 'app-create-teacher',
  templateUrl: './teacher.component.html',
  styleUrls: ['./teacher.component.css']
})
export class CreateTeacherComponent {
  @Input() establishment!: Establishment;
  generatedPassword:string = '';
  generatedUsername:string = '';
  teachers:Teacher[] = [];
  searchError:string = '';
  form:FormGroup = new FormGroup({
    firstName: new FormControl('', [Validators.required]),
    lastName: new FormControl('', [Validators.required]),
    usernameLength: new FormControl([Validators.required, Validators.min(6), Validators.max(12)]),
  });
  searchForm:FormGroup = new FormGroup({
    firstName: new FormControl('', [Validators.required]),
    lastName: new FormControl('', [Validators.required])
  })
  isLoading: boolean = false;
  constructor(private establishmentService:EstablishmentService,private requestService:RequestService, 
    private router:Router, 
    private messageService:MessageService) {
    this.setGeneratedPassword();
    this.setGeneratedUsername();
  }
  setGeneratedPassword = async () => {
      this.generatedPassword = crypto.getRandomValues(new Uint32Array(8))[0].toString();
  }

  setGeneratedUsername = async () => {
    this.form.get('usernameLength')?.valueChanges.subscribe((value) => {
      console.log(value);
      
        this.generatedUsername = new Date().getUTCFullYear().toString().substring(2,4) + 
        crypto.randomUUID().toString().replace('-','').substring(0, value - 2);
    })
  }

  getGeneratedPassword = () => {
    return this.generatedPassword;
  }

  getGeneratedUsername = () => {
    return this.generatedUsername;
  }

  validateSearch = () => {
    return this.searchForm.valid;
  }

  search = async () => {
  if(!this.validateSearch()) {
    this.searchError = "Veuillez remplir tous les champs";
    return;
  }
  this.teachers = [];
  this.searchError = '';
  this.isLoading = true;
  const res = await this.requestService.getRequest('admin/getTeachersByName/' + 
  this.searchForm.get('firstName')?.value + '/' + this.searchForm.get('lastName')?.value);
  if(isError(res)) {
    this.searchError = parseError(res);
    return;
  }
  let data = ((res as ApiResponse).data as Teacher[])
  let set = new Set<string>(data.map((teacher:Teacher) => teacher.id));
  set.forEach((id:string) => {
    this.teachers.push(data.find((teacher:Teacher) => teacher.id == id)!);
  })
  this.isLoading = false;
  }

  createTeacher = async () => {
    if(!this.validateCreateTeacher()) {
      this.sendError("Veuillez remplir tous les champs");
      return;
    };
    let teacher:Teacher = {
      firstName: this.form.get('firstName')?.value,
      lastName: this.form.get('lastName')?.value
    } as Teacher;
    this.addTeacher(teacher);
  }

  addTeacher = async (teacher:Teacher) => {

    let body ={}
    if(teacher.username != undefined) {
      body = {
        ...teacher,
        establishmentId: this.establishment!.id,
        password: "NOPWDSPECIFIED",
      }
    } else {
    body = {
      ...teacher,
      establishmentId: this.establishment!.id,
      password: this.generatedPassword,
      username: this.generatedUsername
    }
  }
    console.log(body);
    
    let res = await this.requestService.postRequest<typeof body>('admin/createTeacher', body)
    if(isError(res)) {
      this.sendError(parseError(res));
      return;
    }
    this.router.navigate(['/establishment/'+ this.establishment!.id]);
  }

  validateCreateTeacher = () => {
    return this.form.valid;
  }
  loading = () => {
    return this.isLoading;
  }

  sendError(error:string) {
    this.messageService.add({severity:'error', summary:'Error', detail:error, life: 5000});
  }
}

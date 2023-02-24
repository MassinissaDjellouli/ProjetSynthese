import { Component, Input } from '@angular/core';
import { EstablishmentService } from '../../../../../services/establishment/establishment.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Student } from '../../../../../interfaces/Student';
import { RequestService, isError, parseError } from '../../../../../services/request/request.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { Establishment } from '../../../../../interfaces/Establishment';
import { ApiResponse } from '../../../../../interfaces/ApiResponse';

@Component({
  selector: 'app-create-student',
  templateUrl: './student.component.html',
  styleUrls: ['./student.component.css']
})
export class CreateStudentComponent {
  generatedPassword:string = '';
  generatedUsername:string = '';
  students:Student[] = [];
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
  @Input() establishment!: Establishment;
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
  this.students = [];
  this.searchError = '';
  this.isLoading = true;
  const res = await this.requestService.getRequest('admin/getStudentsByName/' + 
  this.searchForm.get('firstName')?.value + '/' + this.searchForm.get('lastName')?.value);
  if(isError(res)) {
    this.searchError = parseError(res);
    return;
  }
  let data = ((res as ApiResponse).data as Student[])
  let set = new Set<string>(data.map((student:Student) => student.id));
  set.forEach((id:string) => {
    this.students.push(data.find((student:Student) => student.id == id)!);
  })
  this.isLoading = false;
  }

  createStudent = async () => {
    if(!this.validateCreateStudent()) {
      this.sendError("Veuillez remplir tous les champs");
      return;
    };
    let student:Student = {
      firstName: this.form.get('firstName')?.value,
      lastName: this.form.get('lastName')?.value
    } as Student;
    this.addStudent(student);
  }

  addStudent = async (student:Student) => {

    let body ={}
    if(student.username != undefined) {
      body = {
        ...student,
        establishmentId: this.establishment!.id,
        password: "NOPWDSPECIFIED",
      }
    } else {
    body = {
      ...student,
      establishmentId: this.establishment!.id,
      password: this.generatedPassword,
      username: this.generatedUsername
    }
  }
    console.log(body);
    
    let res = await this.requestService.postRequest<typeof body>('admin/createStudent', body)
    if(isError(res)) {
      this.sendError(parseError(res));
      return;
    }
    this.router.navigate(['/establishment/'+ this.establishment!.id]);
  }

  validateCreateStudent = () => {
    return this.form.valid;
  }
  loading = () => {
    return this.isLoading;
  }

  sendError(error:string) {
    this.messageService.add({severity:'error', summary:'Error', detail:error, life: 5000});
  }
}

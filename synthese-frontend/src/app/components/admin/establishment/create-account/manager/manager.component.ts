import { Component, Input } from '@angular/core';
import { Establishment } from 'src/app/interfaces/Establishment';
import { RequestService, isError, parseError } from 'src/app/services/request/request.service';
import { Manager } from '../../../../../interfaces/Manager';
import { ApiResponse } from 'src/app/interfaces/ApiResponse';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { EstablishmentService } from 'src/app/services/establishment/establishment.service';
import { MessageService } from 'primeng/api';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-manager',
  templateUrl: './manager.component.html',
  styleUrls: ['./manager.component.css']
})
export class CreateManagerComponent {
  @Input() establishment!: Establishment;
  generatedPassword:string = '';
  generatedUsername:string = '';
  managers:Manager[] = [];
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
  this.managers = [];
  this.searchError = '';
  this.isLoading = true;
  const res = await this.requestService.getRequest('admin/getManagersByName/' + 
  this.searchForm.get('firstName')?.value + '/' + this.searchForm.get('lastName')?.value);
  if(isError(res)) {
    this.searchError = parseError(res);
    return;
  }
  let data = ((res as ApiResponse).data as Manager[])
  let set = new Set<string>(data.map((student:Manager) => student.id));
  set.forEach((id:string) => {
    this.managers.push(data.find((student:Manager) => student.id == id)!);
  })
  this.isLoading = false;
  }

  createManager = async () => {
    if(!this.validateCreateManager()) {
      this.sendError("Veuillez remplir tous les champs");
      return;
    };
    let student:Manager = {
      firstName: this.form.get('firstName')?.value,
      lastName: this.form.get('lastName')?.value
    } as Manager;
    this.addManager(student);
  }

  addManager = async (manager:Manager) => {

    let body ={}
    if(manager.username != undefined) {
      body = {
        ...manager,
        establishmentId: this.establishment!.id,
        password: "NOPWDSPECIFIED",
      }
    } else {
    body = {
      ...manager,
      establishmentId: this.establishment!.id,
      password: this.generatedPassword,
      username: this.generatedUsername
    }
  }
    let res = await this.requestService.postRequest<typeof body>('admin/createManager', body)
    if(isError(res)) {
      this.sendError(parseError(res));
      return;
    }
    this.router.navigate(['/establishment/'+ this.establishment!.id]);
  }

  validateCreateManager = () => {
    return this.form.valid;
  }
  loading = () => {
    return this.isLoading;
  }

  sendError(error:string) {
    this.messageService.add({severity:'error', summary:'Error', detail:error, life: 5000});
  }
}

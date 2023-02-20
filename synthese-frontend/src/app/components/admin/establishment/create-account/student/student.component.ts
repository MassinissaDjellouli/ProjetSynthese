import { Component } from '@angular/core';
import { Establishment } from '../../../../../interfaces/Establishment';
import { EstablishmentService } from '../../../../../services/establishment/establishment.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-create-student',
  templateUrl: './student.component.html',
  styleUrls: ['./student.component.css']
})
export class CreateStudentComponent {
  generatedPassword:string = '';
  generatedUsername:string = '';
  form:FormGroup = new FormGroup({
    firstName: new FormControl('', [Validators.required]),
    lastName: new FormControl('', [Validators.required]),
    usernameLength: new FormControl([Validators.required, Validators.min(6), Validators.max(12)]),
  });
  constructor(private establishmentService:EstablishmentService) {
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
}

import { Component } from '@angular/core';
import { LoginService } from '../../../services/login/login.service';
import { FormControl } from '@angular/forms';
import { Validators } from '@angular/forms';
import { Router } from '@angular/router';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent {
  username = new FormControl('',[Validators.required,Validators.minLength(6),Validators.maxLength(12)]);
  password = new FormControl('',[Validators.required,Validators.minLength(8)]);
  constructor(private loginService:LoginService,router: Router){
    if(loginService.selectMultiple){
      router.navigate(["/selectUser"]);
    }
  }
  validate = () => {
    if(!this.username.valid){
      this.fieldError = "Ce matricule n'est pas valide. (Entre 6-12 chars)"
      return false;
    }
    if(!this.password.valid){
      this.fieldError = "Ce mot de passe est invalide. (Min 8 chars)"
      return false;
    }
    this.fieldError = "";
    return true;
  }
  getLoginType = () => this.loginService.loginType as string;
  login = () => {
    if(!this.validate()){
      return;
    }
    this.loginService.login({username:this.username.value!,password:this.password.value!})
  }
  fieldError = "";
  getError = () => {return this.fieldError == "" ? this.loginService.error:this.fieldError};
}

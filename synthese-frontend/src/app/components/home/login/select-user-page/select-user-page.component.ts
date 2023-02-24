import { Component } from '@angular/core';
import { LoginService } from '../../../../services/login/login.service';
import { LoggedInService } from '../../../../services/login/loggedIn/logged-in.service';
import { Router } from '@angular/router';
import { EstablishmentService } from '../../../../services/establishment/establishment.service';
import { Establishment } from 'src/app/interfaces/Establishment';
import { LoadingService } from '../../../../services/loading/loading.service';

@Component({
  selector: 'app-select-user-page',
  templateUrl: './select-user-page.component.html',
  styleUrls: ['./select-user-page.component.css']
})
export class SelectUserPageComponent {
  refresher = setInterval(() => {
    if(this.getEstablishments().length > 0){
      clearInterval(this.refresher);
    }
  },1000)
  establishments:Establishment[] = [];
  constructor(private loginService:LoginService,private loggedInService:LoggedInService, private loadingService:LoadingService,
    private router:Router,
    private establishmentService:EstablishmentService) { 
      
    if(!this.loginService.selectMultiple){
      console.log("Redirecting to home");
      this.router.navigate(["/"]);
    }
    
    this.setEstablishements();
  }

  select = (est:Establishment) => {
    this.loggedInService.login({
      ...this.loginService.multipleUserCommonInfo,
      userInfo:this.loginService.getMultipleUsers().find((user) => user.establishmentId == est.id)
    });
    this.loginService.selectMultiple = false;
    this.loginService.multipleUserCommonInfo = undefined;
    this.loginService.userChoices = [];
    this.router.navigate(["/"]);
  }
  private setEstablishements = async () => {
    this.loadingService.startLoading();
    let accounts = this.loginService.getMultipleUsers().map((user) => user.establishmentId);
    console.log(this.loginService.getMultipleUsers());
    let establishments = await this.establishmentService.getAllEstablishments()
    
    this.establishments = establishments.filter((establishment) => accounts.includes(establishment.id!));
    this.loadingService.stopLoading();
    console.log(accounts);

  }

  getEstablishments = () => {
    return this.establishments;
  }
}

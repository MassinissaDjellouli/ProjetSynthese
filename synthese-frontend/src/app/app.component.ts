import { Component,OnInit } from '@angular/core';
import { LoadingService } from './services/loading/loading.service';
import { LoggedInService } from './services/login/loggedIn/logged-in.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'synthese-frontend';
  constructor(private loadingServ:LoadingService,loggedInService:LoggedInService){
      loggedInService.loginFromCookie();
  }
  
  loading = () => {
    return this.loadingServ.isLoading()? "d-block":"d-none";  
  }
  notLoading = () => {
    return this.loadingServ.isLoading()? "d-none":"d-block";  
  }
}

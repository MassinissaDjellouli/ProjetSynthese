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
  constructor(private loadingServ:LoadingService,private loggedInService:LoggedInService){
      loggedInService.loginFromCookie();
  }
  
  loading = this.loadingServ.isLoading
}

import { Component } from '@angular/core';
import { LoadingService } from './services/loading/loading.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'synthese-frontend';
 
  constructor(private loadingServ:LoadingService){

  }

  loading = this.loadingServ.isLoading
}

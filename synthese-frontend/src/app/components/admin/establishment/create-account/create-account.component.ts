import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Establishment } from '../../../../interfaces/Establishment';
import { EstablishmentService } from 'src/app/services/establishment/establishment.service';
import { LoadingService } from '../../../../services/loading/loading.service';

@Component({
  selector: 'app-create-account',
  templateUrl: './create-account.component.html',
  styleUrls: ['./create-account.component.css']
})
export class CreateAccountComponent {
  userType:string = "";
  establishment:Establishment | undefined;
  constructor(route:ActivatedRoute, private establishmentService: EstablishmentService,private loadingService:LoadingService) { 
    loadingService.startLoading();
    route.params.subscribe(params => {
      this.userType = params['type'];
      console.log(params);
      
      loadingService.stopLoading();
    })
  }
  getUserType = () => {
    console.log(this.userType);
    
    return this.userType;
  }

}

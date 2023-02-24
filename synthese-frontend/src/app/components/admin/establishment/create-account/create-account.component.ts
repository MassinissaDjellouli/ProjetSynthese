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
  constructor(router:Router,route:ActivatedRoute, establishmentService: EstablishmentService,loadingService:LoadingService) { 
    loadingService.startLoading();
    route.params.subscribe(params => {
      this.userType = params['type'];
      this.establishment = establishmentService.getEstablishment(params['id']);
      loadingService.stopLoading();
      if(this.establishment == undefined){
        router.navigate(['/']);
      }
    })
  }
  hasEstablishment = () => this.establishment != undefined;
  getUserType = () => {
    console.log(this.userType);
    
    return this.userType;
  }

}

import { Injectable, AfterViewChecked } from '@angular/core';
import { LoadingService } from '../loading/loading.service';
import { RequestService, isError, parseError } from '../request/request.service';
import { Establishment } from 'src/app/interfaces/Establishment';
import { ApiResponse } from 'src/app/interfaces/ApiResponse';
import { LoggedInService } from '../login/loggedIn/logged-in.service';
import { Roles } from 'src/app/interfaces/Roles';
import { ActivatedRoute } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class EstablishmentService implements AfterViewChecked{
  establishments:Establishment[] = [];
  error:string = "";
  constructor(    
    private loadingService:LoadingService,
    private requestService:RequestService,
    private loggedInService:LoggedInService,
    private route:ActivatedRoute) {
      if(this.loggedInService.currentLoggedInUser == undefined || this.loggedInService.currentLoggedInUser.role != Roles.Admin){
        return;
      }
     }
  ngAfterViewChecked(): void {
    this.setEstablishments(this.loggedInService.currentLoggedInUser!.userInfo.id);
  }
  setEstablishments = async (id:string) => {
    try{
      this.establishments = await this.fetchEstablishments(id);
    }catch{}
  }
  getEstablishments = ():Establishment[] => {
    return this.establishments;
  }
  private fetchEstablishments = async (adminId:string):Promise<Establishment[]> => {
    this.loadingService.startLoading();
    try{
      const res = await this.requestService.getRequest("admin/getEstablishmentByAdminId/"+adminId)
      if(isError(res)){
        this.error = parseError(res);
        this.loadingService.stopLoading();
        return [];
      }
      this.loadingService.stopLoading();
      return (res as ApiResponse).data as Establishment[];
    }catch{
      this.loadingService.stopLoading();
      return [];
    }
  }


}

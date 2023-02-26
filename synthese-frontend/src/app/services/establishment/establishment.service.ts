import { Injectable, AfterViewChecked } from '@angular/core';
import { LoadingService } from '../loading/loading.service';
import { RequestService, isError, parseError } from '../request/request.service';
import { Establishment } from 'src/app/interfaces/Establishment';
import { ApiResponse } from 'src/app/interfaces/ApiResponse';
import { LoggedInService } from '../login/loggedIn/logged-in.service';
import { Roles } from 'src/app/interfaces/Roles';
import { ActivatedRoute, NavigationEnd, Params, Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class EstablishmentService implements AfterViewChecked{
  establishments:Establishment[] = [];
  error:string = "";
  currentEstablishment:Establishment | undefined;
  constructor(    
    private loadingService:LoadingService,
    private requestService:RequestService,
    private loggedInService:LoggedInService) {
      if(this.loggedInService.currentLoggedInUser == undefined || this.loggedInService.currentLoggedInUser.role != Roles.Admin){
        return;
      }
     }
  ngAfterViewChecked(): void {
    this.setEstablishments(this.loggedInService.currentLoggedInUser!.userInfo.id);
  }
  getAllEstablishments = async () => {
    try{
      const res = await this.requestService.getRequest("establishments")
      if(isError(res)){
        this.error = parseError(res);
        this.loadingService.stopLoading();
        return [];
      }
      return (res as ApiResponse).data as Establishment[];
    }catch{
      return [];
    }
  }
  setEstablishments = async (id:string) => {
    try{
      if(this.loggedInService.currentLoggedInUser?.userInfo.id != id || this.loggedInService.currentLoggedInUser?.role != Roles.Admin){
        return
      }
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

  getEstablishment = (id:string):Establishment | undefined => {
    return this.establishments.find(establishment => establishment.id == id);
  }
}

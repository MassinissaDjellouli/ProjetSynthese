import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LoadingService {
  private loading:boolean = false;
  isLoading = ():boolean => {
    return this.loading;
  }
  startLoading = () => {
    this.loading = true;
  }

  stopLoading = () => {
    this.loading = false;
  }
  constructor() { }
}

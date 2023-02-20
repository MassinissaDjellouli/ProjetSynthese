import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { AdminComponent } from './components/admin/admin.component';
import { ConfigureComponent } from './components/admin/configure/configure.component';
import { EstablishmentComponent } from './components/admin/establishment/establishment.component';
import { CreateAccountComponent } from './components/admin/establishment/create-account/create-account.component';

const routes: Routes = [
  //everyone routes
  { path: '', component: HomeComponent },
  //admin routes
  { path: 'configure', component: ConfigureComponent },
  { path: 'establishment/:id', component: EstablishmentComponent },
  { path: 'establishment/:id/createAccount/:type', component: CreateAccountComponent },
  //student routes
  //teacher routes
  //manager routes
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

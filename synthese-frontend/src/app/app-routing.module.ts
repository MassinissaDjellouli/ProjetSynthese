import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { AdminComponent } from './components/admin/admin.component';
import { ConfigureComponent } from './components/admin/configure/configure.component';
import { EstablishmentComponent } from './components/admin/establishment/establishment.component';
import { CreateAccountComponent } from './components/admin/establishment/create-account/create-account.component';
import { SelectUserPageComponent } from './components/home/login/select-user-page/select-user-page.component';
import { ModifyComponent } from './components/admin/establishment/modify/modify.component';
import { AssignComponent } from './components/manager/assign/assign.component';
import { CreateClassesComponent } from './components/manager/create-classes/create-classes.component';

const routes: Routes = [
  //everyone routes
  { path: '', component: HomeComponent },
  { path: 'selectUser', component: SelectUserPageComponent },
  //admin routes
  { path: 'configure', component: ConfigureComponent },
  { path: 'establishment/:id', component: EstablishmentComponent },
  { path: 'establishment/:id/createAccount/:type', component: CreateAccountComponent },
  { path: 'establishment/:id/modify', component:ModifyComponent},
  //student routes
  //teacher routes
  //manager routes
  { path: 'assignClasses', component: AssignComponent},
  { path: 'addClasses', component: CreateClassesComponent}
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

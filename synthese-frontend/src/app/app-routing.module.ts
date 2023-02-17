import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { AdminComponent } from './components/admin/admin.component';
import { ConfigureComponent } from './components/admin/configure/configure.component';

const routes: Routes = [
  // { path: '', component: AdminComponent },
  { path: 'configure', component: ConfigureComponent },
   { path: '', component: HomeComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

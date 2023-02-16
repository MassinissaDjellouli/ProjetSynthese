import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {FileUploadModule} from 'primeng/fileupload';
import {HttpClientModule} from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { HomeComponent } from './components/home/home.component';
import { CardModule } from 'primeng/card';
import {InputTextModule} from 'primeng/inputtext';
import {ButtonModule} from 'primeng/button';
import {CheckboxModule} from 'primeng/checkbox';
import {RadioButtonModule} from 'primeng/radiobutton';
import {RippleModule} from 'primeng/ripple';
import {FormsModule} from '@angular/forms';
import { LoginComponent } from './components/home/login/login.component';
import {TabMenuModule} from 'primeng/tabmenu';
import {ProgressSpinnerModule} from 'primeng/progressspinner';
import { ReactiveFormsModule } from '@angular/forms';
import { AdminComponent } from './components/admin/admin.component';
import { StudentComponent } from './components/student/student.component';
import { TeacherComponent } from './components/teacher/teacher.component';
import { ManagerComponent } from './components/manager/manager.component';
import { ConfigureComponent } from './components/admin/configure/configure.component';
@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    HomeComponent,
    LoginComponent,
    AdminComponent,
    StudentComponent,
    TeacherComponent,
    ManagerComponent,
    ConfigureComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    CardModule,
    InputTextModule,
		CheckboxModule,
		ButtonModule,
		RadioButtonModule,
    RippleModule,
    FormsModule,
    TabMenuModule,
    ProgressSpinnerModule,
    ReactiveFormsModule,
    FileUploadModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

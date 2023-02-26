import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
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
import {ScrollerModule} from 'primeng/scroller';
import {SelectButtonModule} from 'primeng/selectbutton';
import {MultiSelectModule} from 'primeng/multiselect';
import {TableModule} from 'primeng/table';
import { ScrollTopModule } from "primeng/scrolltop";
import { ScrollPanelModule } from "primeng/scrollpanel";
import { EstablishmentComponent } from './components/admin/establishment/establishment.component';
import { CreateAccountComponent } from './components/admin/establishment/create-account/create-account.component';
import { CreateStudentComponent } from './components/admin/establishment/create-account/student/student.component';
import { CreateTeacherComponent } from './components/admin/establishment/create-account/teacher/teacher.component';
import { CreateManagerComponent } from './components/admin/establishment/create-account/manager/manager.component';
import {ToastModule} from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { SelectUserPageComponent } from './components/home/login/select-user-page/select-user-page.component';
import { ModifyComponent } from './components/admin/establishment/modify/modify.component';
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
    ConfigureComponent,
    EstablishmentComponent,
    CreateAccountComponent,
    CreateStudentComponent,
    CreateTeacherComponent,
    CreateManagerComponent,
    SelectUserPageComponent,
    ModifyComponent
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
    HttpClientModule,
    ScrollerModule,
    SelectButtonModule,
    MultiSelectModule,
    BrowserAnimationsModule,
    ScrollTopModule,
    ScrollPanelModule,
    TableModule,
    ToastModule
  ],
  providers: [MessageService],
  bootstrap: [AppComponent]
})
export class AppModule { }

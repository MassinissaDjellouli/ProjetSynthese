import { Component } from '@angular/core';
import { Course } from 'src/app/interfaces/Course';
import { LoadingService } from 'src/app/services/loading/loading.service';
import { ApiResponse } from '../../../interfaces/ApiResponse';
import { Manager } from '../../../interfaces/Manager';
import { Program } from '../../../interfaces/Program';
import { Teacher } from '../../../interfaces/Teacher';
import { LoggedInService } from '../../../services/login/loggedIn/logged-in.service';
import { RequestService, isError } from '../../../services/request/request.service';
import { getEstablishmentId } from 'src/app/utils/establishmentUtil';

@Component({
  selector: 'app-assign',
  templateUrl: './assign.component.html',
  styleUrls: ['./assign.component.css']
})
export class AssignComponent {
  constructor(private loggedInService:LoggedInService,private requestService:RequestService, private loadingService:LoadingService) {
    this.setPrograms();
  }
  teachers: Teacher[] = [];
  availableTeachers: Teacher[] = [];
  chosenTeachers: Teacher[] = [];
  selectedProgram: Program | undefined;
  selectedCourse: Course | undefined;
  hasPrograms = () => this.programs.length > 0;
  courses:Course[] = [];
  programs: Program[] = [];
  selectProgram = (program: Program) => {
    this.selectedProgram = program;
    this.setCourses();
  }
  selectCourse = (course: Course) => {
    this.selectedCourse = course;
    this.setTeachers();
  }
  getPrograms = () => this.programs;
  setPrograms = async () => {
    this.loadingService.startLoading();
    this.programs = [];
    let res = await this.requestService.getRequest("manager/establishment/" + getEstablishmentId(this.loggedInService) + "/programs")
    if (isError(res)) {
      this.loadingService.stopLoading();
      return;
    }
    this.programs = (res as ApiResponse).data as Program[];
    this.loadingService.stopLoading();
  }
  hasCourses = () => this.courses.length > 0;
  getCourses = () => this.courses;
  setCourses = async () => {
    this.loadingService.startLoading();
    this.courses = [];
    let res = await this.requestService.getRequest("manager/program/" + this.selectedProgram!.id + "/courses")
    if (isError(res)) {
      this.loadingService.stopLoading();
      return;
    }
    this.courses = (res as ApiResponse).data as Course[];
    this.loadingService.stopLoading();
  }
  setTeachers = async () => {
    this.loadingService.startLoading();
    this.teachers = [];
    let res = await this.requestService.getRequest("manager/establishment/"+ getEstablishmentId(this.loggedInService) +"/teachers")
    if (isError(res)) {
      this.loadingService.stopLoading();
      return;
    }
    this.teachers = (res as ApiResponse).data as Teacher[];
    this.loadingService.stopLoading();
    this.setAvailableTeachers();
  }
  currentlySelected = () => this.selectedCourse !== undefined ? "Teacher" : 
  this.selectedProgram !== undefined ? "Course" : "";
  hasTeachers = () => this.teachers.length > 0 || this.chosenTeachers.length > 0;
  clearCourse = () => this.selectedCourse = undefined;
  clearProgram = () => this.selectedProgram = undefined;
  assign = async () => {
    if(this.chosenTeachers.length > 0){
      this.loadingService.startLoading();
      let res = await this.requestService.putRequest("manager/course/" + this.selectedCourse!.id + "/addTeachers",
       this.chosenTeachers.map(teacher => teacher.id))
      if (isError(res)) {
        this.loadingService.stopLoading();
        return;
      }
      this.loadingService.stopLoading();
      this.chosenTeachers = [];
      this.selectedCourse = undefined;
    }
    this.clearCourse();
    this.clearProgram();

  } 
  getHoursPerWeek = (teacher:Teacher) => {
    let hours = 0;
    console.log(teacher);
    
    teacher.courses.forEach(course => hours += course.hoursPerWeek);
    return hours;
  }
  setAvailableTeachers = () => {
    let classHours = this.selectedCourse!.hoursPerWeek;
    this.availableTeachers = this.teachers.filter(teacher => {
      let hours = this.getHoursPerWeek(teacher);
      return hours + classHours <= 40;
    })
  }


}
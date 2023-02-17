import { Component } from '@angular/core';
import { Program } from 'src/app/interfaces/Program';
import { ParseTreeResult, XmlParser } from '@angular/compiler';
import { FormGroup, FormControl, Validators, ValidatorFn, AbstractControl, ValidationErrors } from '@angular/forms';
import { LoadingService } from '../../../services/loading/loading.service';
import { Establishment } from '../../../interfaces/Establishment';
import { Teacher } from '../../../interfaces/Teacher';

@Component({
  selector: 'app-configure',
  templateUrl: './configure.component.html',
  styleUrls: ['./configure.component.css']
})
export class ConfigureComponent {
  configuration: FormGroup = new FormGroup({
    name: new FormControl('',Validators.required),
    address: new FormControl('',Validators.required),
    phone: new FormControl('',Validators.required),
    sessionDuration: new FormControl(6,[Validators.required,Validators.min(1),Validators.max(12)]),
    openTime: new FormControl('',Validators.required),
    closeTime: new FormControl('',Validators.required),
    classStart: new FormControl('',Validators.required),
    dinnerTime: new FormControl(60,[Validators.required,Validators.min(1),Validators.max(999)]),
    periodsLength: new FormControl(60,[Validators.required,Validators.min(1),Validators.max(999)]),
    periodsPerDay: new FormControl(1,[Validators.required,Validators.min(1),Validators.max(999)]),
    periodsBeforeDinner: new FormControl(1,[Validators.required,Validators.min(1),Validators.max(999)]),
    pauseTime: new FormControl(5,[Validators.required,Validators.min(1),Validators.max(999)])
  })

  constructor(private loadingService:LoadingService) { }

  uploadedFiles: any[] = []
  programList: Program[] = [];
  programListError: string = '';
  disabled = false;
  selectedDays:string[] = [];
  DAYS_OF_THE_WEEK = [
    { name: 'Dimanche' },
    { name: 'Lundi' },
    { name: 'Mardi' },
    { name: 'Mercredi' },
    { name: 'Jeudi' },
    { name: 'Vendredi' },
    { name: 'Samedi' }
  ]
  
  isDisabled = () => {
    return this.disabled
  }
  upload = (event: any) => {
    let file = event.files[0]
    try {
      if ((file.name as string).endsWith('.json')) {
        this.setProgramListFromJson(file)
        return
      }
      if ((file.name as string).endsWith('.xml')) {
        this.setProgramListFromXML(file)
        return
      }
    } catch (error) {
      this.invalidFileFormat(error)
      return;
    }

    
  }
  setProgramListFromJson = (file: any) => {
    let reader: FileReader = new FileReader()
    reader.onload = (event: any) => {
      try {
        let result = JSON.parse(event.target.result).programs as Program[]
        this.setProgramList(result)
      } catch (error) {
        this.invalidFileFormat(error)
        return;
      }
    }
    reader.readAsText(file)
  }
  processRootNode = (node: any, rootNodeName: string) => {
    if (node.name == undefined || node.name.toLowerCase() != rootNodeName.toLowerCase()) {
      return
    }
    let list:Program[] = []
    node.children.forEach(
      (child: any) => {
        if (child.name == undefined) {
          return
        }
        let program = {};
        program = this.processChildNode(child)
        list.push(program as Program)
      })
      this.setProgramList(list)
  }
  hasChildren = (node: any): boolean => {
    let hasChildren = false;
    if (node.children == undefined || node.children.length == 0) {
      return false;
    }
    node.children.forEach((child: any) => {
      if (child.name != undefined) {
        hasChildren = true
        return
      }
      if (child.value != undefined && !(child.value as string).includes("\n")) {
        hasChildren = true
        return
      }
    })
    return hasChildren
  }
  processChildNode = (node: any): any => {
    if (node.name == undefined && ((node.value as string).includes("\n"))) {
      return;
    }
    if (node.value != undefined && !node.value.includes("\n")) {
      return node.value
    }
    if (!this.hasChildren(node)) {
      return node.value != undefined ? node.value : []
    }
    let content: any = {}
    node.children.forEach(
      (child: any) => {
        if (child.name != undefined) {
          content[child.name.toLowerCase()] = this.processChildNode(child)
        }
        if (child.value != undefined && !child.value.includes("\n")) {
          content = this.processChildNode(child)
        }
      }
    )
    return content
  }
  getUploadedFiles = () => {
    return this.uploadedFiles
  }
  setProgramListFromXML = async (file: any) => {
    let parser: XmlParser = new XmlParser()
    let reader: FileReader = new FileReader()
    reader.onload = (event: any) => {
      let xmlString: string = event.target.result
      let result: ParseTreeResult = parser.parse(xmlString, 'text/xml')
      result.rootNodes.forEach((node) => this.processRootNode(node, "programs"))
    }
    reader.readAsText(file)
    reader.onloadend = () => {
      try {
        
        if (this.programList == undefined || this.programList.length == 0) {
          this.invalidFileFormat()
          return;
        }
      } catch (error) {
        this.invalidFileFormat(error)
        return;
      }
    }
  }
  invalidFileFormat = (error:any = 'Invalid file format') => {
    console.log(error);
    this.programListError = 'Invalid file format'
  }

  setProgramList = (programList: Program[]) => {
    if(programList == undefined || programList.length == 0){
      this.invalidFileFormat()
      return
    }
    this.programList = programList
    this.programListError = ''
    this.disabled = true
  }
  save = () => {
    this.loadingService.startLoading()
    let establishment:Establishment = {
      name: this.configuration.get('name')?.value,
      address: this.configuration.get('address')?.value,
      phone: this.configuration.get('phone')?.value,
      sessionLength: this.configuration.get('sessionDuration')?.value,
      openTime: this.configuration.get('openTime')?.value,
      closeTime: this.configuration.get('closeTime')?.value,
      classesStartTime: this.configuration.get('classStart')?.value,
      dinnerLength: this.configuration.get('dinnerTime')?.value,
      periodsPerDay: this.configuration.get('periodsPerDay')?.value,
      periodsBeforeDinner: this.configuration.get('periodsBeforeDinner')?.value,
      betweenPeriodsLength: this.configuration.get('pauseTime')?.value,
      periodLength: this.configuration.get('periodLength')?.value,
      daysPerWeek: this.selectedDays,
      programs: this.programList,
      id: 0,
      managers: [],
      students: [],
      teachers: [],
    }
    this.loadingService.stopLoading()
  }
}

import { Component } from '@angular/core';
import { Teacher } from '../../../interfaces/Teacher';
import { Program } from 'src/app/interfaces/Program';
import { ParseTreeResult, XmlParser } from '@angular/compiler';

@Component({
  selector: 'app-configure',
  templateUrl: './configure.component.html',
  styleUrls: ['./configure.component.css']
})
export class ConfigureComponent {
  uploadedFiles: any[] = []
  programList: Program[] = [];
  programListError: string = '';
  upload = (event: any) => {
    let file = event.files[0]
    try {
      if ((file.name as string).endsWith('.json')) {
        let reader: FileReader = new FileReader()
        reader.onload = (event: any) => {
          this.programList = JSON.parse(event.target.result).programs as Program[]
        }
        reader.readAsText(file)
        return
      }
      if ((file.name as string).endsWith('.xml')) {
        this.setProgramListFromXML(file)
        return
      }
    } catch (error) {
      console.log(error);
      this.programListError = 'Invalid file format'
      return;
    }
  }
  processRootNode = (node: any,rootNodeName:string) => {
    if (node.name != rootNodeName) {
      return
    }
    node.children.forEach(
      (child: any) => {
        if (child.name == undefined) {
          return
        }
        console.log(child);
        let program = {};
        program = this.processChildNode(child)
        this.programList.push(program as Program)
      })
      console.log(this.programList);
      
  }
  hasChildren = (node: any): boolean => {
    let hasChildren = false;
    if(node.children == undefined || node.children.length == 0) {
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
    if(node.value != undefined && !node.value.includes("\n")) {
      return node.value
    }
    if (!this.hasChildren(node)) {
      console.log(node);
      return node.value != undefined ? node.value : []
    }
    let content: any = {}
    node.children.forEach(
      (child: any) => {
        if(child.name != undefined) {
            content[child.name] = this.processChildNode(child)
        }
        if(child.value != undefined && !child.value.includes("\n")) {
          content = this.processChildNode(child)
        }
      }
    )
    return content
  }

  setProgramListFromXML = async (file: any) => {
    let parser: XmlParser = new XmlParser()
    let reader: FileReader = new FileReader()
    let parsedXmlAsJson: any = { programs: [] }
    reader.onload = (event: any) => {
      let xmlString: string = event.target.result
      let result: ParseTreeResult = parser.parse(xmlString, 'text/xml')
      result.rootNodes.forEach((node) => this.processRootNode(node,"programs"))
    }
    reader.readAsText(file)
    reader.onloadend = () => {
      this.programList = parsedXmlAsJson.programs as Program[]
    }
  }
}

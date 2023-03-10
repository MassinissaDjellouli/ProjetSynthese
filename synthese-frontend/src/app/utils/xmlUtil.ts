import { ParseTreeResult, XmlParser } from "@angular/compiler"
import { Program } from "../interfaces/Program"
import { Course } from "../interfaces/Course"
import { Observable } from 'rxjs';

const processNode = <T>(node: any, rootNodeName: string) => {
  if (node.name == undefined || node.name.toLowerCase() != rootNodeName.toLowerCase()) {
    return
  }
  let list: T[] = []
  node.children.forEach(
    (child: any) => {
      if (child.name == undefined) {
        return
      }
      let node = {};
      node = processChildNode(child)

      list.push(node as T)
      console.log(list);
    })
  return list;
}
const hasChildren = (node: any): boolean => {
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
const processChildNode = (node: any): any => {
  if (node.name == undefined && ((node.value as string).includes("\n"))) {
    return;
  }
  if (node.value != undefined && !node.value.includes("\n")) {
    return node.value
  }
  if (!hasChildren(node)) {
    return node.value != undefined ? node.value : []
  }
  let content: any = {}
  node.children.forEach(
    (child: any) => {
      if (child.name != undefined) {
        content[child.name.toLowerCase()] = processChildNode(child)
      }
      if (child.value != undefined && !child.value.includes("\n")) {
        content = processChildNode(child)
      }
    }
  )
  return content
}
export const setupXMLReader = <T>(rootNodeName: string) => {
  let parser: XmlParser = new XmlParser()
  let reader: FileReader = new FileReader()
  console.log("setupXMLReader");

  let obs: Observable<T> = new Observable((event: any) => {
    console.log("setupXMLReader3");
    let xmlString: string = event.target.result
    console.log("setupXMLReader4");
    let result: ParseTreeResult = parser.parse(xmlString, 'text/xml')
    console.log(result);
    let list: any = []
    console.log(result);
    result.rootNodes.forEach((node) => {
      let res = processNode<T>(node, rootNodeName)
      if (res != undefined) {
        list = res
      }
      console.log("44tsetupXMLReader");
    })
    return list;
  })
  reader.onload = (event: any) => {
    obs.subscribe((e) => console.log("t:" + e))
  }
  return reader
}


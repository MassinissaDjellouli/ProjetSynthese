import { Course } from "./Course";
import { CourseType } from "./CourseType";
import { Teacher } from "./Teacher";

export interface Program {
    id: string;
    name: string;
    description: string;
    teachers: Teacher[];
    courses: Course[];
    type: CourseType;
    
}
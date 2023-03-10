import { Course } from "./Course";
import { ProgramType } from "./ProgramType";
import { Teacher } from "./Teacher";

export interface Program {
    id: string;
    name: string;
    description: string;
    type: ProgramType;
    
}
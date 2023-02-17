import { Manager } from "./Manager";
import { Program } from "./Program";
import { Student } from "./Student";
import { Teacher } from "./Teacher";

export interface Establishment {
    id: string;
    name: string;
    address: string;
    phone: string;
    programs: Program[];
    sessionLength: number;
    teachers: Teacher[];
    managers: Manager[];
    students: Student[];
    periodLength: number;
    periodsPerDay: number;
    daysPerWeek: string[];
    openTime: string;
    closeTime: string;
    classesStartTime: string;
    periodsBeforeDinner:number;
    dinnerLength: number;
    betweenPeriodsLength: number;
}
export enum Gender {
    MALE = 'MALE',
    FEMALE = 'FEMALE',
    OTHER = 'OTHER'
}

export interface Student {
    reg_no: number;
    roll_no: number;
    name: string;
    standard: number;
    school: string;
    gender: Gender;
    percentage: number;
}
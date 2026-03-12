import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Gender, Student } from '../models/student.model';

@Injectable({ providedIn: 'root' })
export class StudentService {
  baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  getStudents(): Observable<Student[]> {
    return this.http.get<Student[]>(`${this.baseUrl}/students`);
  }

  getById(reg_no: number): Observable<Student> {
    return this.http.get<Student>(`${this.baseUrl}/students/${reg_no}`);
  }

  addStudent(student: Student): Observable<string> {
    return this.http.post(`${this.baseUrl}/students`, student, { responseType: 'text' });
  }

  updateStudent(reg_no: number, student: Student): Observable<string> {
    return this.http.put(`${this.baseUrl}/students/${reg_no}`, student, { responseType: 'text' });
  }

  partialUpdate(reg_no: number, partial: Partial<Student>): Observable<string> {
    return this.http.patch(`${this.baseUrl}/students/${reg_no}`, partial, { responseType: 'text' });
  }

  deleteStudent(reg_no: number): Observable<string> {
    return this.http.delete(`${this.baseUrl}/students/${reg_no}`, { responseType: 'text' });
  }

  searchBySchool(name: string): Observable<Student[]> {
    return this.http.get<Student[]>(`${this.baseUrl}/students/school?name=${name}`);
  }

  countBySchool(name: string): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/students/school/count?name=${name}`);
  }

  countByStandard(standard: number): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/students/school/standard/count?standard=${standard}`);
  }

  getStrength(gender: Gender, standard: number): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/students/strength?gender=${gender}&standard=${standard}`);
  }

  getResult(pass: boolean): Observable<Student[]> {
    return this.http.get<Student[]>(`${this.baseUrl}/students/result?pass=${pass}`);
  }
}
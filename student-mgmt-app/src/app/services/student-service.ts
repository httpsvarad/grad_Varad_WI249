import { Injectable } from '@angular/core';
import { Student } from '../models/student';

@Injectable({
  providedIn: 'root',
})
export class StudentService {

  students: Student[] = [
    {regNo: "S001", rollNo: 1, name: "Varad", standard: 9, school: "DAV"},
    {regNo: "S002", rollNo: 2, name: "Sia", standard: 10, school: "DAV"},
    {regNo: "S003", rollNo: 3, name: "Rahul", standard: 9, school: "TPS"}
  ]

  getStudents() {
    return this.students;
  }

  addStudent(student: Student) {
    this.students.push(student)
  }

  updateStudent(editIndex: number, student: Student) {
    this.students[editIndex] = student
  }

  deleteStudent(deleteIndex: number) {
    this.students.splice(deleteIndex, 1);
  }

}

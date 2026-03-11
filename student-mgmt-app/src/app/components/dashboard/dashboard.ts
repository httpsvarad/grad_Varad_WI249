import { Component } from '@angular/core';
import { StudentService } from '../../services/student-service';
import { Student } from '../../models/student';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard {

  students: Student[] = []
  editIndex: number = -1

  constructor(private studentService: StudentService) {
    this.students = studentService.getStudents()
  }

  saveStudent(e: any) {

    e.preventDefault()

    const student: Student = {
      regNo: e.target.elements[0].value,
      rollNo: e.target.elements[1].value,
      name: e.target.elements[2].value,
      standard: e.target.elements[3].value,
      school: e.target.elements[4].value
    }

    if (student.regNo === '' || student.rollNo === null || student.name === '' || student.school === '' || student.standard === null) {
      alert("All fields are Required!")
      return
    }

    if (this.editIndex === -1) {
      this.studentService.addStudent(student)
    } else {
      this.studentService.updateStudent(this.editIndex, student)
      this.editIndex = -1
    }

    e.target.reset()

  }

  editStudent(index: number) {
    const student = this.students[index]
    const inputs = document.querySelectorAll("input")
    inputs[0].value = student.regNo
    inputs[1].value = student.rollNo.toString()
    inputs[2].value = student.name
    inputs[3].value = student.standard.toString()
    inputs[4].value = student.school
    this.editIndex = index
  }

  deleteStudent(index: number) {
    if (window.confirm("Are you sure?")) {
      this.studentService.deleteStudent(index)
    }
  }

}
import { Component } from '@angular/core';
import { Student } from '../../models/student';
import { CommonModule } from '@angular/common';
import { StudentService } from '../../services/student-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-student-list',
  imports: [CommonModule],
  templateUrl: './student-list.html',
  styleUrl: './student-list.css',
})

export class StudentList {
  students: Student[] = []
  constructor(private stdService: StudentService) {
    this.students = stdService.getStudents()
  }
}

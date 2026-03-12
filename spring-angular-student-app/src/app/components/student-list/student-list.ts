import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Student } from '../../models/student.model';
import { StudentService } from '../../services/student-service';

@Component({
  selector: 'app-student-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './student-list.html',
  styleUrls: ['./student-list.css']
})
export class StudentList implements OnInit {
  students: Student[] = [];
  schoolSearch = '';
  message = '';
  isError = false;

  constructor(private studentService: StudentService) { }

  ngOnInit() { this.loadStudents(); }

  loadStudents() {
    this.studentService.getStudents().subscribe(res => this.students = res);
  }

  deleteStudent(id: number) {
    if (!confirm('Delete student?')) return;
    this.studentService.deleteStudent(id).subscribe({
      next: msg => { this.showMsg(msg, false); this.loadStudents(); },
      error: () => this.showMsg('Delete failed', true)
    });
  }

  searchSchool() {
    if (!this.schoolSearch.trim()) { this.loadStudents(); return; }
    this.studentService.searchBySchool(this.schoolSearch).subscribe(res => this.students = res);
  }

  showPass() { this.studentService.getResult(true).subscribe(res => this.students = res); }
  showFail() { this.studentService.getResult(false).subscribe(res => this.students = res); }

  showMsg(msg: string, err: boolean) {
    this.message = msg; this.isError = err;
    setTimeout(() => this.message = '', 3000);
  }
}
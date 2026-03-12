import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Gender, Student } from '../../models/student.model';
import { StudentList } from '../student-list/student-list';
import { StudentService } from '../../services/student-service';
import { StatsPanel } from '../stats-panel/stats-panel';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, StudentList, StatsPanel],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css']
})
export class Dashboard {
  
  // @ViewChild(StudentList) studentList!: StudentList;

  // ngOnInit(): void {
  //   // Don't call ViewChild here
  // }

  // ngAfterViewInit(): void {
  //   this.studentList?.loadStudents();
  // }

  activeTab: 'list' | 'add' | 'edit' | 'stats' = 'list';
  Gender = Gender;
  message = '';
  isError = false;

  emptyStudent = (): Student => ({
    reg_no: 0, roll_no: 0, name: '', standard: 0,
    school: '', gender: Gender.MALE, percentage: 0
  });

  student: Student = this.emptyStudent();

  // Edit state
  editRegNo: number | null = null;
  editStudent: Student = this.emptyStudent();

  // Patch state
  patchRegNo: number | null = null;
  patchField: keyof Student = 'name';
  patchValue: any = '';
  patchFields: (keyof Student)[] = ['roll_no', 'name', 'standard', 'school', 'gender', 'percentage'];

  constructor(private studentService: StudentService) { }

  setTab(tab: 'list' | 'add' | 'edit' | 'stats') {
    this.activeTab = tab;
    this.message = '';
  }

  onAdd() {
    this.studentService.addStudent(this.student).subscribe({
      next: msg => {
        this.showMsg(msg, false);
        this.student = this.emptyStudent();
        // this.studentList?.loadStudents();
      },
      error: () => this.showMsg('Error adding student', true)
    });
  }

  loadForEdit() {
    if (!this.editRegNo) return;
    this.studentService.getById(this.editRegNo).subscribe({
      next: s => { this.editStudent = { ...s }; this.showMsg('', false); },
      error: () => this.showMsg('Student not found', true)
    });
  }

  onUpdate() {
    if (!this.editRegNo) return;
    this.studentService.updateStudent(this.editRegNo, this.editStudent).subscribe({
      next: msg => { this.showMsg(msg, false) },
      // next: msg => { this.showMsg(msg, false); this.studentList?.loadStudents(); },
      error: () => this.showMsg('Error updating student', true)
    });
  }

  onPatch() {
    if (!this.patchRegNo) return;
    const payload: Partial<Student> = { [this.patchField]: this.patchValue };
    this.studentService.partialUpdate(this.patchRegNo, payload).subscribe({
      // next: msg => { this.showMsg(msg, false); this.studentList?.loadStudents(); },
      next: msg => { this.showMsg(msg, false) },
      error: () => this.showMsg('Error patching student', true)
    });
  }

  showMsg(msg: string, err: boolean) {
    this.message = msg; this.isError = err;
    if (msg) setTimeout(() => this.message = '', 3000);
  }
}
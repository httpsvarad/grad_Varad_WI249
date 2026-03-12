import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Gender } from '../../models/student.model';
import { StudentService } from '../../services/student-service';

@Component({
  selector: 'app-stats-panel',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './stats-panel.html',
  styleUrls: ['./stats-panel.css']
})
export class StatsPanel {
  Gender = Gender;

  schoolName = ''; schoolCount: number | null = null;
  stdNumber: number | null = null; stdCount: number | null = null;
  strengthGender: Gender = Gender.MALE; strengthStd: number | null = null; strengthCount: number | null = null;

  constructor(private studentService: StudentService) {}

  getSchoolCount() {
    if (!this.schoolName) return;
    this.studentService.countBySchool(this.schoolName).subscribe(n => this.schoolCount = n);
  }

  getStdCount() {
    if (!this.stdNumber) return;
    this.studentService.countByStandard(this.stdNumber).subscribe(n => this.stdCount = n);
  }

  getStrength() {
    if (!this.strengthStd) return;
    this.studentService.getStrength(this.strengthGender, this.strengthStd).subscribe(n => this.strengthCount = n);
  }
}
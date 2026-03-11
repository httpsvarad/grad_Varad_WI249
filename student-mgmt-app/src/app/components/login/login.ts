import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth-service';

@Component({
  selector: 'app-login',
  imports: [],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  constructor(private re: Router, private authService: AuthService) { }

  handleSubmit(e: any) {

    e.preventDefault();
    let role: string = (e.target.elements[0].value)
    let user: string = (e.target.elements[1].value)
    let password: string = (e.target.elements[2].value)

    if (user == password) {
      this.authService.setUser(user);
      this.authService.setRole(role);
      if (role == 'Admin') {
        this.re.navigate(['dashboard'])
      } else {
        this.re.navigate(['students'])
      }
    } else {
      this.re.navigate(['login'])
      alert("Inavlid Credentials!")
    }
  }
}

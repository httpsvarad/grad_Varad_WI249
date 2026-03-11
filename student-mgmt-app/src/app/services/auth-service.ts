import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  user: string = ""
  role: string = ""

  setUser(user: string) {
    this.user = user
  }

  getUser() {
    return this.user
  }

  setRole(role: string) {
    this.role = role
  }

  getRole() {
    return this.role
  }

  isLoggedIn(): boolean {
    if (this.user !== "") return true
    return false
  }

}

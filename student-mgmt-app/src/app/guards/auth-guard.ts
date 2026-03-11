import { inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { AuthService } from '../services/auth-service';

export const authGuard: CanActivateFn = (route, state) => {
  const authService: AuthService = inject(AuthService);
  const allowedRoles: string[] = route.data?.['roles'] ?? [];
  const userRole = authService.getRole();

  if (!authService.isLoggedIn()) {
    console.log("Access Denied: User not logged in");
    return false;
  }

  if (allowedRoles.length > 0 && !allowedRoles.includes(userRole)) {
    console.log(`Access Denied: ${userRole} is not allowed`);
    return false;
  }

  return true;
};
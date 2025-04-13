import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  let authService = inject(AuthService);
  let routerService = inject(Router);

  let isUserLoggedIn = authService.isLoggedIn() && !authService.isTokenExpired();

  if(!isUserLoggedIn) {
    routerService.navigate(['/login']);
    return false;
  }
  return true;
};

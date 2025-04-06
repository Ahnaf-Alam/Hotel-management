import { isPlatformBrowser } from '@angular/common';
import { HttpInterceptorFn } from '@angular/common/http';
import { inject, PLATFORM_ID } from '@angular/core';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const jwtToken = getJwtToken();

  if(jwtToken) {
    const cloned = req.clone({
      setHeaders: {
        Authorization: `Bearer ${jwtToken}`,
      },
    });

    return next(cloned);
  }
  return next(req);
};


function getJwtToken(): string | null {
  const platFromId = inject(PLATFORM_ID);
  let token = null;

  if(isPlatformBrowser(platFromId)) {
    token = localStorage.getItem("JWT_TOKEN");
  }
  return token;
}
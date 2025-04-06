import { DOCUMENT, isPlatformBrowser } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Inject, inject, Injectable, PLATFORM_ID } from '@angular/core';
import { jwtDecode } from 'jwt-decode';
import { BehaviorSubject, Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly JWT_TOKEN = 'JWT_TOKEN';
  private loggedUser?: string;
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(false);

  private http = inject(HttpClient);
  private platFromId = inject(PLATFORM_ID);

  constructor() { 
  }

  login(user: {
    email: string, password: string
  }): Observable<any> {
    return this.http.post('https://api.escuelajs.co/api/v1/auth/login', user) // for testing purpose
    .pipe(tap((tokens: any)=> this.doLoginUser(user.email, tokens.access_token)));
  }

  private doLoginUser(email: string, tokens: any) {
    this.loggedUser = email;
    this.storeJwtToken(tokens);
    this.isAuthenticatedSubject.next(true);
  }

  private storeJwtToken(jwt: string) {
    if(isPlatformBrowser(this.platFromId)) {
      localStorage.setItem(this.JWT_TOKEN, jwt);
    }
  }

  getCurrentAuthUser() {
    return this.http.get('https://api.escuelajs.co/api/v1/auth/profile'); // for testing purpose
  }

  logout() {
    if(isPlatformBrowser(this.platFromId)) {
      localStorage.removeItem(this.JWT_TOKEN);
    }
    this.isAuthenticatedSubject.next(false);
  }

  isLoggedIn() {
    if(isPlatformBrowser(this.platFromId)) {
      return !!localStorage.getItem(this.JWT_TOKEN);
    }
    return false;
  }

  isTokenExpired() {
    let token = null;

    if(isPlatformBrowser(this.platFromId)) {
      token = localStorage.getItem(this.JWT_TOKEN);
    }
    if(!token) return true;

    const decoded = jwtDecode(token);
    if(!decoded.exp) return true;
    const expirationDate = decoded.exp * 1000;
    const now = new Date().getTime();

    return expirationDate < now;
  }
}

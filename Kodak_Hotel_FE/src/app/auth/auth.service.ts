import { DOCUMENT, isPlatformBrowser } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Inject, inject, Injectable, PLATFORM_ID } from '@angular/core';
import { jwtDecode } from 'jwt-decode';
import { BehaviorSubject, isObservable, Observable, tap } from 'rxjs';
import { environment } from '../services/environment';
import { Login } from '../models/login';
import { User } from '../models/user';


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly JWT_TOKEN = 'JWT_TOKEN';
  private loggedUser?: string;
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(false);

  private http = inject(HttpClient);
  private platFromId = inject(PLATFORM_ID);
  private baseUrl = environment.apiUrl;

  constructor() { 
  }

  login(user: {
    email: string, password: string
  }): Observable<Login> {
    return this.http.post<Login>(this.baseUrl +'/auth/login', user)
    .pipe(tap((response: Login)=> 
      this.doLoginUser(user.email, response)
    ));
  }

  register(user: User): Observable<any> {
    return this.http.post<any>(environment.apiUrl + '/auth/register', user);
  }

  private doLoginUser(email: string, response: Login) {
    this.loggedUser = email;
    this.storeJwtToken(response.token);
    this.isAuthenticatedSubject.next(true);
  }

  private storeJwtToken(jwt: string) {
    if(isPlatformBrowser(this.platFromId)) {
      localStorage.setItem(this.JWT_TOKEN, jwt);
    }
  }

  getCurrentAuthUser() {
    // return this.http.get('https://api.escuelajs.co/api/v1/auth/profile'); // for testing purpose
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

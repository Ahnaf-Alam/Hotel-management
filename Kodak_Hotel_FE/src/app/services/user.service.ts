import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from './environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  http = inject(HttpClient);
  baseUrl = environment.apiUrl + '/users';

  constructor() { }

  getAllUser(): Observable<any[]> {
    return this.http.get<any[]>(this.baseUrl + '/all');
  }

  getLoggedInUser(): Observable<any> {
    return this.http.get(this.baseUrl + '/get-logged-in-profile-info');
  }
}

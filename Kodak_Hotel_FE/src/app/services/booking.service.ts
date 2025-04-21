import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from './environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BookingService {
  private http = inject(HttpClient);
  private baseUrl = environment.apiUrl + '/booking';
  constructor() { }

  addBooking(roomId: string, userId: string, payload: Object): Observable<any> {
    return this.http.post(`${this.baseUrl}/book-room/${roomId}/${userId}`, payload);
  }
}

import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from './environment';

@Injectable({
  providedIn: 'root'
})
export class RoomService {
  private http = inject(HttpClient);
  private baseUrl = environment.apiUrl + '/rooms';

  constructor() { }

  getAllRooms(): Observable<any[]> {
    return this.http.get<any[]>(this.baseUrl + '/all');
  }

  getRoomTypes(): Observable<string[]> {
    return this.http.get<string[]>(this.baseUrl + '/types');
  }

  getRoomsById(roomId: string): Observable<any> {
    return this.http.get<any>(this.baseUrl + '/room-by-id/' + roomId);
  }

  getRoomByDateAndType(checkIndate: any, checkOutDate: any, type: string): Observable<any[]> {
    let params = new HttpParams();
    params = params.append('checkInDate', checkIndate);
    params = params.append('checkOutDate', checkOutDate);
    params = params.append('roomType', type);
    
    return this.http.get<any[]>(this.baseUrl + '/available-rooms-by-date-and-type', { params: params });
  }
}

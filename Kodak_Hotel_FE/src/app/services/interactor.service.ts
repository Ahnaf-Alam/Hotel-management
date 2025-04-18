import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { Room, RoomFilter } from '../models/room';

@Injectable({
  providedIn: 'root'
})
export class InteractorService {
  private roomFilterValue = new Subject<RoomFilter>();
  roomFilterValue$ = this.roomFilterValue.asObservable();

  constructor() { }

  setRoomFilterValue(checkInDate: string, checkOutDate: string, type: string) {
    const roomFilter: RoomFilter = {
      checkInDate: checkInDate,
      checkOutDate: checkOutDate,
      roomType: type
    }
    this.roomFilterValue.next(roomFilter);
  }
}

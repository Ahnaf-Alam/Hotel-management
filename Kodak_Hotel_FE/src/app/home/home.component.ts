import { Component, inject } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { CommonModule, DatePipe } from '@angular/common';
import { CalendarModule } from 'primeng/calendar';
import { FormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/dropdown';
import { HttpClient } from '@angular/common/http';
import { environment } from '../services/environment';
import { response } from 'express';
import { Observable } from 'rxjs';
import { RoomService } from '../services/room.service';
import { InteractorService } from '../services/interactor.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, CalendarModule, FormsModule, DropdownModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  authService = inject(AuthService);
  http = inject(HttpClient);
  roomService = inject(RoomService);
  interactorService = inject(InteractorService);

  user?: any;
  checkIndate?: string;
  checkOutDate?: string;
  currentDate?: Date;
  checkOutStartDate?: Date;
  roomTypes: string[] = [];
  selectedRoomType?: string;
  baseUrl = environment.apiUrl;

  constructor() {
      this.currentDate = new Date();
      this.getRoomTypes();
  }

  getRoomTypes() {
    this.roomService.getRoomTypes().subscribe((response: string[]) => {
      this.roomTypes = response;
    })
  }

  selectedDate() {
    if(this.checkIndate !== undefined) {
      this.checkOutStartDate = new Date(this.checkIndate);
      this.checkOutStartDate .setDate(this.checkOutStartDate .getDate() + 1);
    }
  }

  onClickSearch() {
    // if(this.checkIndate && this.checkOutDate && this.roomTypes) {
    //   this.interactorService.setRoomFilterValue(this.checkIndate, this.checkOutDate, this.roomTypes);
    // }
  }
}

import { HttpClient } from '@angular/common/http';
import { Component, inject, OnInit } from '@angular/core';
import { RoomService } from '../services/room.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-rooms',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './rooms.component.html',
  styleUrl: './rooms.component.scss'
})
export class RoomsComponent implements OnInit {
  http = inject(HttpClient);
  roomSerivce = inject(RoomService);

  rooms: any[] = [];
  
  ngOnInit(): void {
    this.getAllRooms();

  }

  private getAllRooms() {
    this.roomSerivce.getAllRooms().subscribe((response: any) => {
      this.rooms = response.roomList;

      this.rooms[0].roomPhotoUrl = "./assets/images/room-1.jpg";
      this.rooms[1].roomPhotoUrl = "./assets/images/room-2.jpg";
    });
  }
}

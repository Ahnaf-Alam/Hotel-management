import { HttpClient } from '@angular/common/http';
import { Component, inject, OnInit } from '@angular/core';
import { RoomService } from '../services/room.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { RoomBookingComponent } from './room-booking/room-booking.component';
import { roomBookingConstant } from './room-booking/room-booking.constant';
import { DropdownModule } from 'primeng/dropdown';
import { CalendarModule } from 'primeng/calendar';
import { Room } from '../models/room';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-rooms',
  standalone: true,
  imports: [CommonModule, FormsModule, CalendarModule, DropdownModule],
  templateUrl: './rooms.component.html',
  styleUrl: './rooms.component.scss',
  providers: [DialogService, MessageService]
})
export class RoomsComponent implements OnInit {
  http = inject(HttpClient);
  roomSerivce = inject(RoomService);
  ref: DynamicDialogRef | undefined;
  dialogService = inject(DialogService);
  messageService = inject(MessageService)

  rooms: any[] = [];
  checkInDate?: string;
  checkOutDate?: string;
  checkOutStartDate!: Date;
  currentDate?: Date;
  roomTypes: any[] = [];
  selectedRoomType: any;

  ngOnInit(): void {
    this.currentDate = new Date();
    this.getAllRooms();

  }

  private getAllRooms() {
    this.roomSerivce.getAllRooms().subscribe((response: any) => {
      this.rooms = response.roomList;

      this.rooms[0].roomPhotoUrl = "./assets/images/room-1.jpg";
      this.rooms[1].roomPhotoUrl = "./assets/images/room-2.jpg";
    });
  }

  onClickBooking(selectedRoom: Room) {
    if(this.checkInDate === undefined || this.checkOutDate === undefined) {
      this.messageService.add({summary: 'Please select checkIn/Checkout date', detail: 'Message content', key: 'toast'});
    }
    this.ref = this.dialogService.open(RoomBookingComponent, this.dialogConfig(selectedRoom));
  }

  onClickClose() {
    this.ref?.close();
  }

  selectedDate() {
    if(this.checkInDate !== undefined) {
      this.checkOutStartDate = new Date(this.checkInDate);
      this.checkOutStartDate.setDate(this.checkOutStartDate.getDate() + 1);
    }

    if(this.checkInDate !== undefined && this.checkOutDate !== undefined) {
      this.getAvailableRoomByDateAndType();
    }
  }

  private getAvailableRoomByDateAndType() {
    this.roomSerivce.getRoomByDateAndType(this.checkInDate, this.checkOutDate, this.selectedRoomType)
      .subscribe(response => {
        console.log(response);
      });
  }

  private dialogConfig(selectedRoom: Room) {
    return {
      showHeader: false,
      modal: true,
      baseZIndex: 100000,
      styleClass: 'room-booking',
      data: {
        checkInDate: this.checkInDate,
        checkOutDate: this.checkOutDate,
        roomType: this.selectedRoomType,
        room: selectedRoom
      }
    }
  }
}

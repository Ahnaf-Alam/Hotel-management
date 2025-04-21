import { Component, inject, Input } from '@angular/core';
import { DialogService, DynamicDialogConfig, DynamicDialogModule, DynamicDialogRef } from 'primeng/dynamicdialog';
import { Room } from '../../models/room';
import { BookingService } from '../../services/booking.service';
import { UserService } from '../../services/user.service';
import { response } from 'express';

@Component({
  selector: 'app-room-booking',
  standalone: true,
  imports: [DynamicDialogModule],
  templateUrl: './room-booking.component.html',
  styleUrl: './room-booking.component.scss',
  providers: [DialogService]
})
export class RoomBookingComponent {

  checkInDate!: string;
  checkOutDate!: string;
  roomType?: string;
  room!: Room
  totalDay!: number;
  totalPrice!: number;
  user: any;

  bookingService = inject(BookingService);
  userService = inject(UserService);

  constructor(
    private ref: DynamicDialogRef,
    private config: DynamicDialogConfig
  ) {
    this.checkInDate = this.config.data.checkInDate;
    this.checkOutDate = this.config.data.checkOutDate,
    this.roomType = this.config.data.roomType;
    this.room = this.config.data.room;

    this.calculateTotalDay();
    this.calculateTotalPrice();
    this.getCurrentLoggedInUser();
  }

  onClickClose() {
    this.ref.close();
  }

  onClickConfirm() {
    this.bookingService.addBooking(this.room.id, this.user.id, this.getBookingPayload()).subscribe(response => {
      console.log(response);
    })
  }

  private calculateTotalDay() {
    const startDate: Date = new Date(this.checkInDate);
    const endDate: Date = new Date(this.checkOutDate);
    this.totalDay = endDate.getDate() - startDate.getDate();
  }

  private calculateTotalPrice() {
    this.totalPrice = this.totalDay * this.room?.roomPrice;
  }

  private getBookingPayload() {
    return {
      checkInDate: this.checkInDate,
      checkOutDate: this.checkOutDate,
      numOfAdults: 2,
      numOfChildren: 1,
    }
  }

  // Must need to refactor. Breaking Single Responsibility principle 
  private getCurrentLoggedInUser() {
    this.userService.getLoggedInUser().subscribe(response => {
      this.user = response.userDTO;
    })
  }

}

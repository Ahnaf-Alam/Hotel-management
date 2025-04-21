import { Component, inject, Input } from '@angular/core';
import { DialogService, DynamicDialogConfig, DynamicDialogModule, DynamicDialogRef } from 'primeng/dynamicdialog';
import { Room } from '../../models/room';

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
  }

  onClickClose() {
    this.ref.close();
  }

  private calculateTotalDay() {
    const startDate: Date = new Date(this.checkInDate);
    const endDate: Date = new Date(this.checkOutDate);
    this.totalDay = endDate.getDate() - startDate.getDate();
  }

  private calculateTotalPrice() {
    this.totalPrice = this.totalDay * this.room?.roomPrice;
  }

}

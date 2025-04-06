import { Component, inject } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { CommonModule } from '@angular/common';
import { CalendarModule } from 'primeng/calendar';
import { FormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/dropdown';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, CalendarModule, FormsModule, DropdownModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  authService = inject(AuthService);
  user?: any;
  checkIndate?: Date;
  checkOutDate?: Date;
  currentDate?: Date;
  checkOutStartDate?: Date;
  roomTypes: string[] = [];
  selectedRoomType?: string;

  constructor() {
      this.authService.getCurrentAuthUser().subscribe(response => {
        console.log(response);
        this.user = response;
      });

      this.currentDate = new Date();

      // this is testing pupose. We will update when we add apis
      this.roomTypes.push("Couple");
      this.roomTypes.push("Single");
      this.roomTypes.push("Family suit");
      this.roomTypes.push("President suit");
  }

  selectedDate() {
    if(this.checkIndate !== undefined) {
      this.checkOutStartDate = new Date();
      this.checkOutStartDate.setDate(this.checkIndate?.getDate() + 1);
    }
  }
}

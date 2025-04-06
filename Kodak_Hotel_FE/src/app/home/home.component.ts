import { Component, inject } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  authService = inject(AuthService);
  user?: any;

  constructor() {
      this.authService.getCurrentAuthUser().subscribe(response => {
        console.log(response);
        this.user = response;
      })
  }
}

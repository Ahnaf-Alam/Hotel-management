import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PasswordModule } from 'primeng/password';
import { User } from '../models/user';
import { AuthService } from '../auth/auth.service';
import { tap } from 'rxjs';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { environment } from '../services/environment';

@Component({
  selector: 'app-sign-in',
  standalone: true,
  imports: [CommonModule, PasswordModule, FormsModule],
  templateUrl: './sign-in.component.html',
  styleUrl: './sign-in.component.scss'
})
export class SignInComponent {
    authService = inject(AuthService);
    router = inject(Router);
    http = inject(HttpClient);

    user: User = {
      name: '',
      email: '',
      phoneNumber: '',
      password: ''
    }

    onClickRegister() {
      this.authService.register(this.user)
      .subscribe(response => {
        this.router.navigateByUrl('/login');
      })
    }
}

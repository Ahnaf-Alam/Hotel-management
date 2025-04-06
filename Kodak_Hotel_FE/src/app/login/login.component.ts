import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../auth/auth.service';
import { Router } from '@angular/router';
import { error } from 'console';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
    email = '';
    password = '';
    errorMessage = '';
    isError: boolean = false;

    authService = inject(AuthService)
    routerService = inject(Router)

    onClickLogin() {
      this.authService.login({
        email: this.email,
        password: this.password
      }).subscribe(response => {
        alert("Login success!!");
        this.routerService.navigate(['/']);
      }, (error) => {
        this.isError = true;
        this.errorMessage = "Username or password is invalid"
      }
      )
    }
}

import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../auth/auth.service';

@Component({
  selector: 'kdk-generic-header',
  standalone: true,
  imports: [],
  templateUrl: './generic-header.component.html',
  styleUrl: './generic-header.component.scss'
})
export class GenericHeaderComponent {
  private router = inject(Router)
  private authService = inject(AuthService);

  onClickNavigate(path: string) {
    if(path === 'logout') {
      this.authService.logout();
      this.router.navigateByUrl('login');
    } else {
      this.router.navigateByUrl(path)
    }
  }
}

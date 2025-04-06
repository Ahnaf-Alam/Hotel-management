import { AfterContentChecked, AfterViewChecked, Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, NavigationEnd, Router, RouterModule, RouterOutlet, Event, NavigationStart } from '@angular/router';
import { GenericHeaderComponent } from "./common/generic-header/generic-header.component";
import { AuthService } from './auth/auth.service';
import { filter } from 'rxjs';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, GenericHeaderComponent, RouterModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'Kodak_Hotel_FE';
  private route = inject(Router);
  isNavbarAvailable: boolean = true;

  constructor() {
    this.route.events.subscribe(event => {
      if(event && event instanceof NavigationStart) {
        if(event.url === "/login" || event.url === '/register') {
          this.isNavbarAvailable = false;
        } else this.isNavbarAvailable = true;
      }
    })
  }

}

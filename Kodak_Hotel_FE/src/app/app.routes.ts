import { Routes } from '@angular/router';
import { authGuard } from './auth/auth.guard';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { SignInComponent } from './sign-in/sign-in.component';

export const routes: Routes = [
    { 
        path: 'home',
        loadComponent: () => import('./home/home.component').then(c => c.HomeComponent),
        canActivate: [authGuard] 
    },
    { path: 'login', component: LoginComponent},
    { path: 'register', component: SignInComponent },
    { path: '', redirectTo: '/home', pathMatch: 'full'},
    {
        path: 'rooms',
        loadComponent: () => import('./rooms/rooms.component').then(c => c.RoomsComponent),
        canActivate: [authGuard],
    }
];

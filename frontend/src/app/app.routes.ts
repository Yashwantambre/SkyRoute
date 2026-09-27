import { Routes } from '@angular/router';
import { SearchPage } from './pages/search/search.page';
import { BookingsPage } from './pages/bookings/bookings.page';

export const routes: Routes = [
  { path: '', component: SearchPage },
  { path: 'bookings', component: BookingsPage },
  { path: '**', redirectTo: '' }
];

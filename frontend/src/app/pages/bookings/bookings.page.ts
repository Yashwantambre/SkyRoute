import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DatePipe, DecimalPipe } from '@angular/common';
import { AirlineApiService } from '../../services/airline-api.service';
import { Booking } from '../../models/airline.models';

@Component({
  selector: 'app-bookings-page',
  imports: [FormsModule, DatePipe, DecimalPipe],
  templateUrl: './bookings.page.html',
  styleUrl: './bookings.page.css'
})
export class BookingsPage implements OnInit {
  bookings: Booking[] = [];
  pnr = '';
  searched = false;
  error = '';

  constructor(private readonly api: AirlineApiService) {}

  ngOnInit(): void {
    this.reload();
  }

  search(): void {
    const pnr = this.pnr.trim();
    this.error = '';
    if (!pnr) {
      this.error = 'Enter a PNR.';
      return;
    }
    this.api.getBooking(pnr).subscribe({
      next: (booking) => {
        this.bookings = [booking];
        this.searched = true;
      },
      error: (err) => {
        this.bookings = [];
        this.searched = true;
        this.error = err.error?.message ?? 'Booking not found.';
      }
    });
  }

  showAll(): void {
    this.pnr = '';
    this.searched = false;
    this.error = '';
    this.reload();
  }

  cancel(pnr: string): void {
    this.api.cancelBooking(pnr).subscribe({
      next: () => (this.searched ? this.search() : this.reload()),
      error: (err) => (this.error = err.error?.message ?? 'Cancel failed')
    });
  }

  private reload(): void {
    this.api.listBookings().subscribe({
      next: (bookings) => (this.bookings = bookings),
      error: () => (this.error = 'Unable to load bookings.')
    });
  }
}

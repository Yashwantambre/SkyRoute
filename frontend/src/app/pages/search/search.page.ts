import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DatePipe, DecimalPipe } from '@angular/common';
import { AirlineApiService } from '../../services/airline-api.service';
import { Airport, Flight } from '../../models/airline.models';

@Component({
  selector: 'app-search-page',
  imports: [FormsModule, DatePipe, DecimalPipe],
  templateUrl: './search.page.html',
  styleUrl: './search.page.css'
})
export class SearchPage implements OnInit {
  airports: Airport[] = [];
  flights: Flight[] = [];
  origin = 'DEL';
  destination = 'BOM';
  travelDate = this.defaultDate();
  seats = 1;
  passengerName = '';
  email = '';
  phone = '';
  selectedFlight: Flight | null = null;
  cabinFilter: 'ALL' | Flight['cabinClass'] = 'ALL';
  searched = false;
  message = '';
  error = '';

  constructor(private readonly api: AirlineApiService) {}

  ngOnInit(): void {
    this.api.listAirports().subscribe({
      next: (airports) => (this.airports = airports),
      error: () => (this.error = 'Unable to load airports. Start the SkyRoute API on port 8080.')
    });
  }

  get visibleFlights(): Flight[] {
    if (this.cabinFilter === 'ALL') {
      return this.flights;
    }
    return this.flights.filter((flight) => flight.cabinClass === this.cabinFilter);
  }

  get estimatedTotal(): number {
    if (!this.selectedFlight) {
      return 0;
    }
    const seats = Number(this.seats) || 0;
    return this.selectedFlight.price * seats;
  }

  search(keepMessage = false): void {
    this.error = '';
    if (!keepMessage) {
      this.message = '';
    }
    this.selectedFlight = null;
    this.cabinFilter = 'ALL';
    this.api.searchFlights(this.origin, this.destination, this.travelDate).subscribe({
      next: (flights) => {
        this.flights = flights;
        this.searched = true;
      },
      error: (err) => {
        this.flights = [];
        this.searched = true;
        this.error = err.error?.message ?? 'Search failed';
      }
    });
  }

  choose(flight: Flight): void {
    this.selectedFlight = flight;
    this.message = '';
    this.error = '';
  }

  book(): void {
    if (!this.selectedFlight) {
      return;
    }
    this.api
      .createBooking({
        flightId: this.selectedFlight.id,
        passengerName: this.passengerName,
        email: this.email,
        phone: this.phone,
        seats: this.seats
      })
      .subscribe({
        next: (booking) => {
          this.message = `Booking confirmed. PNR ${booking.pnr}`;
          this.selectedFlight = null;
          this.search(true);
        },
        error: (err) => (this.error = err.error?.message ?? 'Booking failed')
      });
  }

  private defaultDate(): string {
    const date = new Date();
    date.setDate(date.getDate() + 7);
    return date.toISOString().slice(0, 10);
  }
}

import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Airport, Booking, CreateBookingRequest, Flight } from '../models/airline.models';

@Injectable({ providedIn: 'root' })
export class AirlineApiService {
  constructor(private readonly http: HttpClient) {}

  listAirports() {
    return this.http.get<Airport[]>('/api/airports');
  }

  searchFlights(origin: string, destination: string, travelDate: string) {
    const params = new HttpParams()
      .set('origin', origin)
      .set('destination', destination)
      .set('travelDate', travelDate);
    return this.http.get<Flight[]>('/api/flights/search', { params });
  }

  listBookings() {
    return this.http.get<Booking[]>('/api/bookings');
  }

  getBooking(pnr: string) {
    return this.http.get<Booking>(`/api/bookings/${encodeURIComponent(pnr)}`);
  }

  createBooking(body: CreateBookingRequest) {
    return this.http.post<Booking>('/api/bookings', body);
  }

  cancelBooking(pnr: string) {
    return this.http.post<Booking>(`/api/bookings/${pnr}/cancel`, {});
  }
}

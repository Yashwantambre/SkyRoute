export interface Airport {
  code: string;
  city: string;
  name: string;
}

export interface Flight {
  id: number;
  airline: string;
  flightNumber: string;
  origin: string;
  originCity: string;
  destination: string;
  destinationCity: string;
  departureTime: string;
  arrivalTime: string;
  durationMinutes: number;
  price: number;
  seatsAvailable: number;
  cabinClass: 'ECONOMY' | 'BUSINESS';
}

export interface Booking {
  id: number;
  pnr: string;
  flight: Flight;
  passengerName: string;
  email: string;
  phone: string | null;
  seats: number;
  totalAmount: number;
  status: 'CONFIRMED' | 'CANCELLED';
  bookedAt: string;
}

export interface CreateBookingRequest {
  flightId: number;
  passengerName: string;
  email: string;
  phone?: string;
  seats: number;
}

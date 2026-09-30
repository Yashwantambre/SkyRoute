package com.learn.skyroute.booking;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learn.skyroute.booking.dto.BookingResponse;
import com.learn.skyroute.booking.dto.CreateBookingRequest;
import com.learn.skyroute.error.InvalidRequestException;
import com.learn.skyroute.error.ResourceNotFoundException;
import com.learn.skyroute.flight.Flight;
import com.learn.skyroute.flight.FlightService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {

	private final BookingRepository bookingRepository;
	private final FlightService flightService;

	public List<BookingResponse> list() {
		List<Booking> bookings = bookingRepository.findAllByOrderByBookedAtDesc();
		List<BookingResponse> responses = new ArrayList<>();

		for (Booking booking : bookings) {
			BookingResponse response = BookingResponse.from(booking);
			responses.add(response);
		}

		return responses;
	}

	public BookingResponse getByPnr(String pnr) {
		return BookingResponse.from(findByPnr(pnr));
	}

	@Transactional
	public BookingResponse create(CreateBookingRequest request) {
		if (request.getFlightId() == null) {
			throw new InvalidRequestException("Flight is required");
		}
		String name = requireText(request.getPassengerName(), "Passenger name is required");
		String email = requireText(request.getEmail(), "Email is required");

		int seats;
		if (request.getSeats() == null) {
			seats = 1;
		} else {
			seats = request.getSeats();
		}

		if (seats < 1 || seats > 9) {
			throw new InvalidRequestException("Seats must be between 1 and 9");
		}

		Flight flight = flightService.find(request.getFlightId());
		if (flight.getSeatsAvailable() < seats) {
			throw new InvalidRequestException("Not enough seats available");
		}

		flight.reserveSeats(seats);
		flightService.save(flight);

		BigDecimal total = flight.getPrice().multiply(BigDecimal.valueOf(seats));
		String phone = null;
		if (request.getPhone() != null) {
			phone = request.getPhone().trim();
		}

		Booking booking = new Booking(
				nextPnr(),
				flight,
				name,
				email,
				phone,
				seats,
				total,
				BookingStatus.CONFIRMED,
				LocalDateTime.now());
		Booking savedBooking = bookingRepository.save(booking);
		return BookingResponse.from(savedBooking);
	}

	@Transactional
	public BookingResponse cancel(String pnr) {
		Booking booking = findByPnr(pnr);
		if (booking.getStatus() == BookingStatus.CANCELLED) {
			throw new InvalidRequestException("Booking is already cancelled");
		}
		booking.cancel();
		booking.getFlight().releaseSeats(booking.getSeats());
		flightService.save(booking.getFlight());
		return BookingResponse.from(bookingRepository.save(booking));
	}

	private Booking findByPnr(String pnr) {
		if (pnr == null || pnr.isBlank()) {
			throw new InvalidRequestException("PNR is required");
		}

		Optional<Booking> booking = bookingRepository.findByPnrIgnoreCase(pnr.trim());
		if (booking.isEmpty()) {
			throw new ResourceNotFoundException("Booking " + pnr + " not found");
		}

		return booking.get();
	}

	private String requireText(String value, String message) {
		if (value == null || value.isBlank()) {
			throw new InvalidRequestException(message);
		}
		return value.trim();
	}

	private String nextPnr() {
		return UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
	}

}

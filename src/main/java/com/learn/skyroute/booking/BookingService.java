package com.learn.skyroute.booking;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learn.skyroute.booking.dto.BookingResponse;
import com.learn.skyroute.booking.dto.CreateBookingRequest;
import com.learn.skyroute.error.InvalidRequestException;
import com.learn.skyroute.error.ResourceNotFoundException;
import com.learn.skyroute.flight.Flight;
import com.learn.skyroute.flight.FlightService;

@Service
public class BookingService {

	private final BookingRepository bookingRepository;
	private final FlightService flightService;

	public BookingService(BookingRepository bookingRepository, FlightService flightService) {
		this.bookingRepository = bookingRepository;
		this.flightService = flightService;
	}

	public List<BookingResponse> list() {
		return bookingRepository.findAllByOrderByBookedAtDesc().stream()
				.map(BookingResponse::from)
				.toList();
	}

	public BookingResponse getByPnr(String pnr) {
		return BookingResponse.from(findByPnr(pnr));
	}

	@Transactional
	public BookingResponse create(CreateBookingRequest request) {
		if (request.flightId() == null) {
			throw new InvalidRequestException("Flight is required");
		}
		String name = requireText(request.passengerName(), "Passenger name is required");
		String email = requireText(request.email(), "Email is required");
		int seats = request.seats() == null ? 1 : request.seats();
		if (seats < 1 || seats > 9) {
			throw new InvalidRequestException("Seats must be between 1 and 9");
		}

		Flight flight = flightService.find(request.flightId());
		if (flight.getSeatsAvailable() < seats) {
			throw new InvalidRequestException("Not enough seats available");
		}

		flight.reserveSeats(seats);
		flightService.save(flight);

		BigDecimal total = flight.getPrice().multiply(BigDecimal.valueOf(seats));
		Booking booking = new Booking(
				nextPnr(),
				flight,
				name,
				email,
				request.phone() == null ? null : request.phone().trim(),
				seats,
				total,
				BookingStatus.CONFIRMED,
				LocalDateTime.now());
		return BookingResponse.from(bookingRepository.save(booking));
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
		return bookingRepository.findByPnrIgnoreCase(pnr.trim())
				.orElseThrow(() -> new ResourceNotFoundException("Booking " + pnr + " not found"));
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

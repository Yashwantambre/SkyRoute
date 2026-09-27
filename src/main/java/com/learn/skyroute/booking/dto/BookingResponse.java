package com.learn.skyroute.booking.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.learn.skyroute.booking.Booking;
import com.learn.skyroute.booking.BookingStatus;
import com.learn.skyroute.flight.dto.FlightResponse;

public record BookingResponse(
		Long id,
		String pnr,
		FlightResponse flight,
		String passengerName,
		String email,
		String phone,
		Integer seats,
		BigDecimal totalAmount,
		BookingStatus status,
		LocalDateTime bookedAt) {

	public static BookingResponse from(Booking booking) {
		return new BookingResponse(
				booking.getId(),
				booking.getPnr(),
				FlightResponse.from(booking.getFlight()),
				booking.getPassengerName(),
				booking.getEmail(),
				booking.getPhone(),
				booking.getSeats(),
				booking.getTotalAmount(),
				booking.getStatus(),
				booking.getBookedAt());
	}

}

package com.learn.skyroute.booking.dto;

public record CreateBookingRequest(
		Long flightId,
		String passengerName,
		String email,
		String phone,
		Integer seats) {
}

package com.learn.skyroute.flight.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.learn.skyroute.flight.CabinClass;
import com.learn.skyroute.flight.Flight;

public record FlightResponse(
		Long id,
		String airline,
		String flightNumber,
		String origin,
		String originCity,
		String destination,
		String destinationCity,
		LocalDateTime departureTime,
		LocalDateTime arrivalTime,
		Integer durationMinutes,
		BigDecimal price,
		Integer seatsAvailable,
		CabinClass cabinClass) {

	public static FlightResponse from(Flight flight) {
		return new FlightResponse(
				flight.getId(),
				flight.getAirline(),
				flight.getFlightNumber(),
				flight.getOrigin(),
				flight.getOriginCity(),
				flight.getDestination(),
				flight.getDestinationCity(),
				flight.getDepartureTime(),
				flight.getArrivalTime(),
				flight.getDurationMinutes(),
				flight.getPrice(),
				flight.getSeatsAvailable(),
				flight.getCabinClass());
	}

}

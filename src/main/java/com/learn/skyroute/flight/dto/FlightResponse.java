package com.learn.skyroute.flight.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.learn.skyroute.flight.CabinClass;
import com.learn.skyroute.flight.Flight;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlightResponse {

	private Long id;
	private String airline;
	private String flightNumber;
	private String origin;
	private String originCity;
	private String destination;
	private String destinationCity;
	private LocalDateTime departureTime;
	private LocalDateTime arrivalTime;
	private Integer durationMinutes;
	private BigDecimal price;
	private Integer seatsAvailable;
	private CabinClass cabinClass;

	public static FlightResponse from(Flight flight) {
		FlightResponse response = new FlightResponse();
		response.setId(flight.getId());
		response.setAirline(flight.getAirline());
		response.setFlightNumber(flight.getFlightNumber());
		response.setOrigin(flight.getOrigin());
		response.setOriginCity(flight.getOriginCity());
		response.setDestination(flight.getDestination());
		response.setDestinationCity(flight.getDestinationCity());
		response.setDepartureTime(flight.getDepartureTime());
		response.setArrivalTime(flight.getArrivalTime());
		response.setDurationMinutes(flight.getDurationMinutes());
		response.setPrice(flight.getPrice());
		response.setSeatsAvailable(flight.getSeatsAvailable());
		response.setCabinClass(flight.getCabinClass());
		return response;
	}

}

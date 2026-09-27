package com.learn.skyroute.flight;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.learn.skyroute.error.InvalidRequestException;
import com.learn.skyroute.error.ResourceNotFoundException;
import com.learn.skyroute.flight.dto.FlightResponse;

@Service
public class FlightService {

	private final FlightRepository flightRepository;

	public FlightService(FlightRepository flightRepository) {
		this.flightRepository = flightRepository;
	}

	public List<FlightResponse> search(String origin, String destination, LocalDate travelDate) {
		if (origin == null || origin.isBlank() || destination == null || destination.isBlank() || travelDate == null) {
			throw new InvalidRequestException("Origin, destination and travel date are required");
		}
		String from = origin.trim().toUpperCase();
		String to = destination.trim().toUpperCase();
		if (from.equals(to)) {
			throw new InvalidRequestException("Origin and destination must be different");
		}
		return flightRepository.search(from, to, travelDate).stream()
				.map(FlightResponse::from)
				.toList();
	}

	public FlightResponse get(Long id) {
		return FlightResponse.from(find(id));
	}

	public Flight find(Long id) {
		return flightRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Flight " + id + " not found"));
	}

	public Flight save(Flight flight) {
		return flightRepository.save(flight);
	}

}

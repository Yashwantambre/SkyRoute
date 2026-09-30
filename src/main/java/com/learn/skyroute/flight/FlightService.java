package com.learn.skyroute.flight;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.learn.skyroute.error.InvalidRequestException;
import com.learn.skyroute.error.ResourceNotFoundException;
import com.learn.skyroute.flight.dto.FlightResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FlightService {

	private final FlightRepository flightRepository;

	public List<FlightResponse> search(String origin, String destination, LocalDate travelDate) {
		if (origin == null || origin.isBlank() || destination == null || destination.isBlank() || travelDate == null) {
			throw new InvalidRequestException("Origin, destination and travel date are required");
		}
		String from = origin.trim().toUpperCase();
		String to = destination.trim().toUpperCase();
		if (from.equals(to)) {
			throw new InvalidRequestException("Origin and destination must be different");
		}

		List<Flight> flights = flightRepository.search(from, to, travelDate);
		List<FlightResponse> responses = new ArrayList<>();

		for (Flight flight : flights) {
			FlightResponse response = FlightResponse.from(flight);
			responses.add(response);
		}

		return responses;
	}

	public FlightResponse get(Long id) {
		return FlightResponse.from(find(id));
	}

	public Flight find(Long id) {
		Optional<Flight> flight = flightRepository.findById(id);
		if (flight.isEmpty()) {
			throw new ResourceNotFoundException("Flight " + id + " not found");
		}

		return flight.get();
	}

	public Flight save(Flight flight) {
		return flightRepository.save(flight);
	}

}

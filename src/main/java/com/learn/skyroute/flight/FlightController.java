package com.learn.skyroute.flight;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learn.skyroute.flight.dto.FlightResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
public class FlightController {

	private final FlightService flightService;

	@GetMapping("/search")
	public List<FlightResponse> search(
			@RequestParam String origin,
			@RequestParam String destination,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate travelDate) {
		return flightService.search(origin, destination, travelDate);
	}

	@GetMapping("/{id}")
	public FlightResponse get(@PathVariable Long id) {
		return flightService.get(id);
	}

}

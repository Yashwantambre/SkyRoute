package com.learn.skyroute.flight;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.learn.skyroute.airport.Airport;
import com.learn.skyroute.airport.AirportCatalog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FlightDataLoader implements CommandLineRunner {

	private static final int DAYS_TO_SEED = 30;
	private static final List<Airline> AIRLINES = List.of(
			new Airline("IndiGo", "6E"),
			new Airline("Air India", "AI"),
			new Airline("Akasa Air", "QP"),
			new Airline("SpiceJet", "SG"));

	private final FlightRepository flightRepository;

	@Override
	public void run(String... args) {
		Set<String> existingFlights = new HashSet<>();
		for (Flight flight : flightRepository.findAll()) {
			existingFlights.add(key(flight.getOrigin(), flight.getDestination(), flight.getDepartureTime()));
		}

		List<Flight> flightsToCreate = new ArrayList<>();
		addOriginalSamples(flightsToCreate, existingFlights);
		addMonthlySchedule(flightsToCreate, existingFlights);

		if (!flightsToCreate.isEmpty()) {
			flightRepository.saveAll(flightsToCreate);
		}
	}

	private void addOriginalSamples(List<Flight> flights, Set<String> existingFlights) {
		LocalDate day = LocalDate.now().plusDays(7);
		LocalDate nextDay = day.plusDays(1);

		addIfMissing(flights, existingFlights, flight("IndiGo", "6E 203", "DEL", "Delhi", "BOM", "Mumbai",
				day, LocalTime.of(6, 15), 130, "4899", 42, CabinClass.ECONOMY));
		addIfMissing(flights, existingFlights, flight("Air India", "AI 887", "DEL", "Delhi", "BOM", "Mumbai",
				day, LocalTime.of(9, 40), 135, "5620", 18, CabinClass.ECONOMY));
		addIfMissing(flights, existingFlights, flight("Vistara", "UK 955", "DEL", "Delhi", "BOM", "Mumbai",
				day, LocalTime.of(18, 10), 125, "9140", 8, CabinClass.BUSINESS));
		addIfMissing(flights, existingFlights, flight("IndiGo", "6E 531", "BOM", "Mumbai", "BLR", "Bengaluru",
				day, LocalTime.of(7, 5), 105, "3299", 36, CabinClass.ECONOMY));
		addIfMissing(flights, existingFlights, flight("Akasa Air", "QP 1120", "BOM", "Mumbai", "BLR", "Bengaluru",
				day, LocalTime.of(16, 45), 110, "3540", 24, CabinClass.ECONOMY));
		addIfMissing(flights, existingFlights, flight("IndiGo", "6E 612", "DEL", "Delhi", "GOI", "Goa",
				nextDay, LocalTime.of(11, 20), 150, "6125", 20, CabinClass.ECONOMY));
		addIfMissing(flights, existingFlights, flight("SpiceJet", "SG 8174", "BLR", "Bengaluru", "DEL", "Delhi",
				nextDay, LocalTime.of(8, 0), 165, "4780", 15, CabinClass.ECONOMY));
		addIfMissing(flights, existingFlights, flight("Air India", "AI 440", "HYD", "Hyderabad", "DEL", "Delhi",
				nextDay, LocalTime.of(13, 30), 140, "5199", 22, CabinClass.ECONOMY));
	}

	private void addMonthlySchedule(List<Flight> flights, Set<String> existingFlights) {
		List<Airport> airports = AirportCatalog.airports();
		LocalDate firstDay = LocalDate.now();

		for (int dayOffset = 0; dayOffset < DAYS_TO_SEED; dayOffset++) {
			LocalDate date = firstDay.plusDays(dayOffset);
			for (int originIndex = 0; originIndex < airports.size(); originIndex++) {
				for (int destinationIndex = 0; destinationIndex < airports.size(); destinationIndex++) {
					if (originIndex == destinationIndex) {
						continue;
					}
					addScheduledFlight(flights, existingFlights, airports, originIndex, destinationIndex, date, 0);
					addScheduledFlight(flights, existingFlights, airports, originIndex, destinationIndex, date, 1);
				}
			}
		}
	}

	private void addScheduledFlight(List<Flight> flights, Set<String> existingFlights, List<Airport> airports,
			int originIndex, int destinationIndex, LocalDate date, int slot) {
		Airport origin = airports.get(originIndex);
		Airport destination = airports.get(destinationIndex);
		Airline airline = AIRLINES.get((originIndex * airports.size() + destinationIndex + slot) % AIRLINES.size());

		CabinClass cabinClass;
		if (slot == 0) {
			cabinClass = CabinClass.ECONOMY;
		} else {
			cabinClass = CabinClass.BUSINESS;
		}

		int routeNumber = 100 + (originIndex * airports.size() + destinationIndex) * 2 + slot;
		int hour;
		if (slot == 0) {
			hour = 6 + (originIndex + destinationIndex) % 5;
		} else {
			hour = 16 + (originIndex + destinationIndex) % 5;
		}

		int minute = ((originIndex * 3 + destinationIndex * 2 + slot) % 12) * 5;
		int durationMinutes = 70 + (originIndex * 31 + destinationIndex * 17) % 111;
		long economyPrice = 2400L + durationMinutes * 18L + (originIndex + destinationIndex) * 90L;

		long price;
		int seats;
		if (cabinClass == CabinClass.BUSINESS) {
			price = economyPrice * 2;
			seats = 18;
		} else {
			price = economyPrice;
			seats = 72;
		}

		Flight flight = flight(
				airline.getName(),
				airline.getPrefix() + " " + routeNumber,
				origin.getCode(),
				origin.getCity(),
				destination.getCode(),
				destination.getCity(),
				date,
				LocalTime.of(hour, minute),
				durationMinutes,
				String.valueOf(price),
				seats,
				cabinClass);
		addIfMissing(flights, existingFlights, flight);
	}

	private void addIfMissing(List<Flight> flights, Set<String> existingFlights, Flight flight) {
		String key = key(flight.getOrigin(), flight.getDestination(), flight.getDepartureTime());
		if (existingFlights.add(key)) {
			flights.add(flight);
		}
	}

	private String key(String origin, String destination, LocalDateTime departureTime) {
		return origin + "|" + destination + "|" + departureTime;
	}

	private Flight flight(String airline, String number, String origin, String originCity, String destination,
			String destinationCity, LocalDate date, LocalTime departure, int durationMinutes, String price, int seats,
			CabinClass cabinClass) {
		LocalDateTime departureTime = LocalDateTime.of(date, departure);
		return new Flight(airline, number, origin, originCity, destination, destinationCity, departureTime,
				departureTime.plusMinutes(durationMinutes), durationMinutes, new BigDecimal(price), seats, cabinClass);
	}

	@Getter
	@AllArgsConstructor
	private static class Airline {

		private String name;
		private String prefix;
	}

}

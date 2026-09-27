package com.learn.skyroute.flight;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flight, Long> {

	List<Flight> findByOriginAndDestinationAndDepartureTimeBetweenOrderByDepartureTimeAsc(
			String origin, String destination, LocalDateTime start, LocalDateTime end);

	default List<Flight> search(String origin, String destination, LocalDate travelDate) {
		return findByOriginAndDestinationAndDepartureTimeBetweenOrderByDepartureTimeAsc(
				origin, destination, travelDate.atStartOfDay(), travelDate.plusDays(1).atStartOfDay().minusNanos(1));
	}

}

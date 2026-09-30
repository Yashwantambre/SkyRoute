package com.learn.skyroute.flight;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "flights")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Flight {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String airline;

	@Column(nullable = false)
	private String flightNumber;

	@Column(nullable = false, length = 3)
	private String origin;

	@Column(nullable = false)
	private String originCity;

	@Column(nullable = false, length = 3)
	private String destination;

	@Column(nullable = false)
	private String destinationCity;

	@Column(nullable = false)
	private LocalDateTime departureTime;

	@Column(nullable = false)
	private LocalDateTime arrivalTime;

	@Column(nullable = false)
	private Integer durationMinutes;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal price;

	@Column(nullable = false)
	private Integer seatsAvailable;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private CabinClass cabinClass;

	public Flight(String airline, String flightNumber, String origin, String originCity, String destination,
			String destinationCity, LocalDateTime departureTime, LocalDateTime arrivalTime, Integer durationMinutes,
			BigDecimal price, Integer seatsAvailable, CabinClass cabinClass) {
		this.airline = airline;
		this.flightNumber = flightNumber;
		this.origin = origin;
		this.originCity = originCity;
		this.destination = destination;
		this.destinationCity = destinationCity;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
		this.durationMinutes = durationMinutes;
		this.price = price;
		this.seatsAvailable = seatsAvailable;
		this.cabinClass = cabinClass;
	}

	public void reserveSeats(int seats) {
		this.seatsAvailable -= seats;
	}

	public void releaseSeats(int seats) {
		this.seatsAvailable += seats;
	}

}

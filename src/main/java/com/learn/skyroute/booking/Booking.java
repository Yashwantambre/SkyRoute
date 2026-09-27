package com.learn.skyroute.booking;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.learn.skyroute.flight.Flight;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "bookings")
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 8)
	private String pnr;

	@ManyToOne(optional = false)
	@JoinColumn(name = "flight_id")
	private Flight flight;

	@Column(nullable = false)
	private String passengerName;

	@Column(nullable = false)
	private String email;

	private String phone;

	@Column(nullable = false)
	private Integer seats;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal totalAmount;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private BookingStatus status;

	@Column(nullable = false)
	private LocalDateTime bookedAt;

	protected Booking() {
	}

	public Booking(String pnr, Flight flight, String passengerName, String email, String phone, Integer seats,
			BigDecimal totalAmount, BookingStatus status, LocalDateTime bookedAt) {
		this.pnr = pnr;
		this.flight = flight;
		this.passengerName = passengerName;
		this.email = email;
		this.phone = phone;
		this.seats = seats;
		this.totalAmount = totalAmount;
		this.status = status;
		this.bookedAt = bookedAt;
	}

	public Long getId() {
		return id;
	}

	public String getPnr() {
		return pnr;
	}

	public Flight getFlight() {
		return flight;
	}

	public String getPassengerName() {
		return passengerName;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}

	public Integer getSeats() {
		return seats;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public BookingStatus getStatus() {
		return status;
	}

	public LocalDateTime getBookedAt() {
		return bookedAt;
	}

	public void cancel() {
		this.status = BookingStatus.CANCELLED;
	}

}

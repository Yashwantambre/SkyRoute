package com.learn.skyroute.booking.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.learn.skyroute.booking.Booking;
import com.learn.skyroute.booking.BookingStatus;
import com.learn.skyroute.flight.dto.FlightResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {

	private Long id;
	private String pnr;
	private FlightResponse flight;
	private String passengerName;
	private String email;
	private String phone;
	private Integer seats;
	private BigDecimal totalAmount;
	private BookingStatus status;
	private LocalDateTime bookedAt;

	public static BookingResponse from(Booking booking) {
		BookingResponse response = new BookingResponse();
		response.setId(booking.getId());
		response.setPnr(booking.getPnr());
		response.setFlight(FlightResponse.from(booking.getFlight()));
		response.setPassengerName(booking.getPassengerName());
		response.setEmail(booking.getEmail());
		response.setPhone(booking.getPhone());
		response.setSeats(booking.getSeats());
		response.setTotalAmount(booking.getTotalAmount());
		response.setStatus(booking.getStatus());
		response.setBookedAt(booking.getBookedAt());
		return response;
	}

}

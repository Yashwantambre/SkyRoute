package com.learn.skyroute.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingRequest {

	private Long flightId;
	private String passengerName;
	private String email;
	private String phone;
	private Integer seats;

}

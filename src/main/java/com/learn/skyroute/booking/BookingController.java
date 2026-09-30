package com.learn.skyroute.booking;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learn.skyroute.booking.dto.BookingResponse;
import com.learn.skyroute.booking.dto.CreateBookingRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

	private final BookingService bookingService;

	@GetMapping
	public List<BookingResponse> list() {
		return bookingService.list();
	}

	@GetMapping("/{pnr}")
	public BookingResponse get(@PathVariable String pnr) {
		return bookingService.getByPnr(pnr);
	}

	@PostMapping
	public ResponseEntity<BookingResponse> create(@RequestBody CreateBookingRequest request) {
		BookingResponse created = bookingService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@PostMapping("/{pnr}/cancel")
	public BookingResponse cancel(@PathVariable String pnr) {
		return bookingService.cancel(pnr);
	}

}

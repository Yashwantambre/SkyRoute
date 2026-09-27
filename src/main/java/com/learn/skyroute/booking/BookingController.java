package com.learn.skyroute.booking;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.learn.skyroute.booking.dto.BookingResponse;
import com.learn.skyroute.booking.dto.CreateBookingRequest;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

	private final BookingService bookingService;

	public BookingController(BookingService bookingService) {
		this.bookingService = bookingService;
	}

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
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{pnr}")
				.buildAndExpand(created.pnr())
				.toUri();
		return ResponseEntity.created(location).body(created);
	}

	@PostMapping("/{pnr}/cancel")
	public BookingResponse cancel(@PathVariable String pnr) {
		return bookingService.cancel(pnr);
	}

}

package com.learn.skyroute.airport;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/airports")
public class AirportController {

	@GetMapping
	public List<Airport> list() {
		return AirportCatalog.airports();
	}

}

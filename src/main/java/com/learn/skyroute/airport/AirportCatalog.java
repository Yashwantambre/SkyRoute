package com.learn.skyroute.airport;

import java.util.List;

public final class AirportCatalog {

	private static final List<Airport> AIRPORTS = List.of(
			new Airport("DEL", "Delhi", "Indira Gandhi International"),
			new Airport("BOM", "Mumbai", "Chhatrapati Shivaji Maharaj International"),
			new Airport("BLR", "Bengaluru", "Kempegowda International"),
			new Airport("HYD", "Hyderabad", "Rajiv Gandhi International"),
			new Airport("MAA", "Chennai", "Chennai International"),
			new Airport("CCU", "Kolkata", "Netaji Subhas Chandra Bose International"),
			new Airport("GOI", "Goa", "Goa International"),
			new Airport("PNQ", "Pune", "Pune International"),
			new Airport("AMD", "Ahmedabad", "Sardar Vallabhbhai Patel International"),
			new Airport("COK", "Kochi", "Cochin International"));

	private AirportCatalog() {
	}

	public static List<Airport> airports() {
		return AIRPORTS;
	}
}

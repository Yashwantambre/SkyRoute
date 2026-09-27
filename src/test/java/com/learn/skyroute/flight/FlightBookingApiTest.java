package com.learn.skyroute.flight;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class FlightBookingApiTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void searchesFlightsThenBooksAndCancels() throws Exception {
		String travelDate = LocalDate.now().plusDays(7).toString();

		MvcResult search = mockMvc.perform(get("/api/flights/search")
						.param("origin", "DEL")
						.param("destination", "BOM")
						.param("travelDate", travelDate))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].origin").value("DEL"))
				.andExpect(jsonPath("$[0].destination").value("BOM"))
				.andExpect(jsonPath("$[*].flightNumber").value(hasItem("6E 203")))
				.andReturn();

		Number flightId = JsonPath.read(search.getResponse().getContentAsString(), "$[0].id");

		MvcResult booked = mockMvc.perform(post("/api/bookings")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"flightId":%s,"passengerName":"  Riya Sharma  ","email":"riya@example.com","phone":"9876543210","seats":2}
								""".formatted(flightId)))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"))
				.andExpect(jsonPath("$.passengerName").value("Riya Sharma"))
				.andExpect(jsonPath("$.status").value("CONFIRMED"))
				.andExpect(jsonPath("$.seats").value(2))
				.andReturn();

		String pnr = JsonPath.read(booked.getResponse().getContentAsString(), "$.pnr");

		mockMvc.perform(get("/api/bookings/{pnr}", pnr))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.pnr").value(pnr));

		mockMvc.perform(post("/api/bookings/{pnr}/cancel", pnr))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("CANCELLED"));

		mockMvc.perform(get("/api/bookings/{pnr}", "MISSING"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Booking MISSING not found"));
	}

	@Test
	void rejectsSameOriginAndDestination() throws Exception {
		mockMvc.perform(get("/api/flights/search")
						.param("origin", "DEL")
						.param("destination", "DEL")
						.param("travelDate", LocalDate.now().plusDays(7).toString()))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Origin and destination must be different"));
	}

	@Test
	void rejectsBookingWithoutPassengerName() throws Exception {
		String travelDate = LocalDate.now().plusDays(7).toString();
		MvcResult search = mockMvc.perform(get("/api/flights/search")
						.param("origin", "BOM")
						.param("destination", "BLR")
						.param("travelDate", travelDate))
				.andExpect(status().isOk())
				.andReturn();

		Number flightId = JsonPath.read(search.getResponse().getContentAsString(), "$[0].id");

		mockMvc.perform(post("/api/bookings")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"flightId":%s,"passengerName":"   ","email":"guest@example.com","seats":1}
								""".formatted(flightId)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Passenger name is required"));
	}

}

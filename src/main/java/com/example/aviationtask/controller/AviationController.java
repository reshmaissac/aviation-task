package com.example.aviationtask.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.aviationtask.model.AirportResponse;
import com.example.aviationtask.model.FlightEntity;
import com.example.aviationtask.model.FlightResponse;
import com.example.aviationtask.services.AviationService;

@Controller
class AirportController {

	@Autowired
	AviationService aviationService;

	@GetMapping("/")
	public String home(Model model) {
		List<FlightEntity> flights = aviationService.getFlights();
		model.addAttribute("flights", flights);
		return "index";
	}

	@GetMapping("/flightDetails")
	public String flightDetails(@RequestParam("flightNumber") int flightNumber,
			@RequestParam("departureDate") String departureDate, Model model) {

		FlightResponse flightdetailsResp = aviationService.getFlightDetailsByNumbAndDate(flightNumber, departureDate);

		model.addAttribute("flight", flightdetailsResp.getFlight());
		model.addAttribute("cargoWeight", flightdetailsResp.getCargoWeight());
		model.addAttribute("baggageWeight", flightdetailsResp.getBaggageWeight());
		model.addAttribute("totalWeight", flightdetailsResp.getTotalWeight());

		return "flight-details";
	}

	@GetMapping("/airportDetails")
	public String airportDetails(@RequestParam("airportCode") String airportCode,
			@RequestParam("departureDate") String departureDate, Model model) {
		AirportResponse airportDetailsResp = aviationService.getAirportDetailsByIATACodeAndDate(airportCode,
				departureDate);

		model.addAttribute("airportCode", airportCode);
		model.addAttribute("departureDate", departureDate);
		model.addAttribute("departingFlights", airportDetailsResp.getNumberOfFlightsDeparting());
		model.addAttribute("arrivingFlights", airportDetailsResp.getNumberOfFlightsArriving());
		model.addAttribute("arrivingBaggagePieces", airportDetailsResp.getTotalPiecesOfBaggageArriving());
		model.addAttribute("departingBaggagePieces", airportDetailsResp.getTotalPiecesOfBaggageDeparting());

		return "airport-details";
	}

}
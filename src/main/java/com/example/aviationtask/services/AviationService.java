package com.example.aviationtask.services;

import java.util.List;

import com.example.aviationtask.model.AirportResponse;
import com.example.aviationtask.model.FlightEntity;
import com.example.aviationtask.model.FlightResponse;

public interface AviationService {

	FlightResponse getFlightDetailsByNumbAndDate(int flightNumber, String departureDate);
	AirportResponse getAirportDetailsByIATACodeAndDate(String airportCode, String departureDate	);
	List<FlightEntity> getFlights(); 
}
 
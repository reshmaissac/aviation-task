package com.example.aviationtask.services;

import com.example.aviationtask.dto.AirportResponse;
import com.example.aviationtask.dto.FlightResponse;

public interface AviationService {

	FlightResponse getFlightDetailsByNumbAndDate(int flightNumber, String departureDate);

	AirportResponse getAirportDetailsByIATACodeAndDate(String airportCode, String departureDate);

}

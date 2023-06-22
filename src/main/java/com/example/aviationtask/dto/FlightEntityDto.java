package com.example.aviationtask.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightEntityDto {
	private int flightId;
	private int flightNumber;
	private String departureAirportIATACode;
	private String arrivalAirportIATACode;
	private String departureDate;

	public FlightEntityDto(int flightNumber2, String departureDate2, String departureAirportIATACode2,
			String arrivalAirportIATACode2) {
		this.flightNumber = flightNumber2;
		this.departureDate = departureDate2;
		this.departureAirportIATACode = departureAirportIATACode2;
		this.arrivalAirportIATACode = arrivalAirportIATACode2;
	}
}
package com.example.aviationtask.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class FlightEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private int flightNumber;
	private String departureAirportIATACode;
	private String arrivalAirportIATACode;
	private String departureDate;

	public FlightEntity(long flightId, int flightNumber, String departureAirportIATACode, String arrivalAirportIATACode,
			String departureDate) {
		this.id = flightId;
		this.flightNumber = flightNumber;
		this.departureAirportIATACode = departureAirportIATACode;
		this.arrivalAirportIATACode = arrivalAirportIATACode;
		this.departureDate = departureDate;
	}
}
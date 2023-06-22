package com.example.aviationtask.dto;

import lombok.Data;

@Data
public class FlightResponse {

	private FlightEntityDto flight;
	private int cargoWeight;
	private int baggageWeight;
	private int totalWeight;
}
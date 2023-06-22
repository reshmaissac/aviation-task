package com.example.aviationtask.dto;

import java.util.List;

import lombok.Data;

@Data
public class FlightCargoDto {

	private int flightId;
	private List<BaggageDto> baggage;
	private List<CargoDto> cargo;
}

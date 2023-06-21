package com.example.aviationtask.model;

import lombok.Data;

@Data
public class FlightResponse {

	private FlightEntity flight;
    private int cargoWeight;
    private int baggageWeight;
    private int totalWeight;
}
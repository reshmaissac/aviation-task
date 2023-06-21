package com.example.aviationtask.model;

import lombok.Data;

@Data
public class AirportResponse {

    private int numberOfFlightsDeparting;
    private int numberOfFlightsArriving;
    private int totalPiecesOfBaggageArriving;
    private int totalPiecesOfBaggageDeparting;
}
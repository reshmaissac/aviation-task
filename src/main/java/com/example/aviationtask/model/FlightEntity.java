package com.example.aviationtask.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightEntity {
    private int flightId;
    private int flightNumber;
    private String departureAirportIATACode;
    private String arrivalAirportIATACode;
    private String departureDate;
}
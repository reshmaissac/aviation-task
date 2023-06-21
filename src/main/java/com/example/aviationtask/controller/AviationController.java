package com.example.aviationtask.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.aviationtask.model.Baggage;
import com.example.aviationtask.model.Cargo;
import com.example.aviationtask.model.CargoEntity;
import com.example.aviationtask.model.FlightEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
class AirportController {

    private List<FlightEntity> flights;
    private List<CargoEntity> cargoList;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    private void loadDataFromJson() {
        try {
        	ClassLoader classLoader = getClass().getClassLoader();
            flights = objectMapper.readValue(new File(classLoader.getResource("flights.json").getFile()), new TypeReference<List<FlightEntity>>() {});
            cargoList = objectMapper.readValue(new File(classLoader.getResource("cargo.json").getFile()), new TypeReference<List<CargoEntity>>() {});
            System.out.println("Data Loaded!!!!");
        } catch (IOException e) {
            e.printStackTrace();
            flights = new ArrayList<>();
            cargoList = new ArrayList<>();
        }
    }

    @GetMapping("/")
    public String home(Model model) {
    	System.out.println("HOME!!!!");
        model.addAttribute("flights", flights);
        return "index";
    }

    @GetMapping("/flightDetails")
    public String flightDetails(@RequestParam("flightNumber") int flightNumber,
                                @RequestParam("departureDate") String departureDate,
                                Model model) {
        FlightEntity requestedFlight = null;
        for (FlightEntity flight : flights) {
            if (flight.getFlightNumber() == flightNumber && flight.getDepartureDate().equals(departureDate)) {
                requestedFlight = flight;
                break;
            }
        }

        CargoEntity cargo = getCargoByFlightId(requestedFlight.getFlightId());

        int cargoWeight = calculateCargoWeight(cargo);
        int baggageWeight = calculateBaggageWeight(cargo);
        int totalWeight = cargoWeight + baggageWeight;

        model.addAttribute("flight", requestedFlight);
        model.addAttribute("cargoWeight", cargoWeight);
        model.addAttribute("baggageWeight", baggageWeight);
        model.addAttribute("totalWeight", totalWeight);

        return "flight-details";
    }

    @GetMapping("/airportDetails")
    public String airportDetails(@RequestParam("airportCode") String airportCode,
                                 @RequestParam("departureDate") String departureDate,
                                 Model model) {
        int departingFlights = 0;
        int arrivingFlights = 0;
        int arrivingBaggagePieces = 0;
        int departingBaggagePieces = 0;

        for (FlightEntity flight : flights) {
            if (flight.getDepartureAirportIATACode().equals(airportCode) && flight.getDepartureDate().contains(departureDate)) {
                departingFlights++;
                CargoEntity cargo = getCargoByFlightId(flight.getFlightId());
                departingBaggagePieces += calculateTotalBaggagePieces(cargo);
            }
            if (flight.getArrivalAirportIATACode().equals(airportCode) && flight.getDepartureDate().contains(departureDate)) {
                arrivingFlights++;
                CargoEntity cargo = getCargoByFlightId(flight.getFlightId());
                arrivingBaggagePieces += calculateTotalBaggagePieces(cargo);
            }
        }

        model.addAttribute("airportCode", airportCode);
        model.addAttribute("departureDate", departureDate);
        model.addAttribute("departingFlights", departingFlights);
        model.addAttribute("arrivingFlights", arrivingFlights);
        model.addAttribute("arrivingBaggagePieces", arrivingBaggagePieces);
        model.addAttribute("departingBaggagePieces", departingBaggagePieces);

        return "airport-details";
    }

    private CargoEntity getCargoByFlightId(int flightId) {
        for (CargoEntity cargo : cargoList) {
            if (cargo.getFlightId() == flightId) {
                return cargo;
            }
        }
        return null;
    }

    private int calculateCargoWeight(CargoEntity cargo) {
        int cargoWeight = 0;
        if (cargo != null && cargo.getCargo() != null) {
            for (Cargo cargoItem : cargo.getCargo()) {
                cargoWeight += cargoItem.getWeight();
            }
        }
        return cargoWeight;
    }

    private int calculateBaggageWeight(CargoEntity cargo) {
        int baggageWeight = 0;
        if (cargo != null && cargo.getBaggage() != null) {
            for (Baggage baggage : cargo.getBaggage()) {
                baggageWeight += baggage.getWeight();
            }
        }
        return baggageWeight;
    }

    private int calculateTotalBaggagePieces(CargoEntity cargo) {
        int totalBaggagePieces = 0;
        if (cargo != null && cargo.getBaggage() != null) {
            for (Baggage baggage : cargo.getBaggage()) {
                totalBaggagePieces += baggage.getPieces();
            }
        }
        return totalBaggagePieces;
    }
}
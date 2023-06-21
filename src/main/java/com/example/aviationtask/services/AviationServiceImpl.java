package com.example.aviationtask.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.aviationtask.model.AirportResponse;
import com.example.aviationtask.model.Baggage;
import com.example.aviationtask.model.Cargo;
import com.example.aviationtask.model.CargoEntity;
import com.example.aviationtask.model.FlightEntity;
import com.example.aviationtask.model.FlightResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AviationServiceImpl implements AviationService {

	private List<FlightEntity> flights;
	private List<CargoEntity> cargoList;

	@Autowired
	private ObjectMapper objectMapper;

	@PostConstruct
	private void loadDataFromJson() {
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			flights = objectMapper.readValue(new File(classLoader.getResource("flights.json").getFile()),
					new TypeReference<List<FlightEntity>>() {
					});
			cargoList = objectMapper.readValue(new File(classLoader.getResource("cargo.json").getFile()),
					new TypeReference<List<CargoEntity>>() {
					});
			System.out.println("Data Loaded!!");
		} catch (IOException e) {
			e.printStackTrace();
			flights = new ArrayList<>();
			cargoList = new ArrayList<>();
		}
	}

	@Override
	public FlightResponse getFlightDetailsByNumbAndDate(int flightNumber, String departureDate) {
		FlightResponse flightResponse = new FlightResponse();
		FlightEntity requestedFlight = null;
		for (FlightEntity flight : flights) {
			if (flight.getFlightNumber() == flightNumber && flight.getDepartureDate().equals(departureDate)) {
				requestedFlight = flight;
				break;
			}
		}

		CargoEntity cargo = getCargoByFlightId(requestedFlight.getFlightId(), cargoList);

		int cargoWeight = calculateCargoWeight(cargo);
		int baggageWeight = calculateBaggageWeight(cargo);
		int totalWeight = cargoWeight + baggageWeight;

		flightResponse.setFlight(requestedFlight);
		flightResponse.setCargoWeight(cargoWeight);
		flightResponse.setBaggageWeight(baggageWeight);
		flightResponse.setTotalWeight(totalWeight);
		return flightResponse;
	}

	@Override
	public AirportResponse getAirportDetailsByIATACodeAndDate(String airportCode, String departureDate) {

		AirportResponse airportResponse = new AirportResponse();

		int departingFlights = 0;
		int arrivingFlights = 0;
		int arrivingBaggagePieces = 0;
		int departingBaggagePieces = 0;

		for (FlightEntity flight : flights) {
			if (flight.getDepartureAirportIATACode().equals(airportCode)
					&& flight.getDepartureDate().contains(departureDate)) {
				departingFlights++;
				CargoEntity cargo = getCargoByFlightId(flight.getFlightId(), cargoList);
				departingBaggagePieces += calculateTotalBaggagePieces(cargo);
			}
			if (flight.getArrivalAirportIATACode().equals(airportCode)
					&& flight.getDepartureDate().contains(departureDate)) {
				arrivingFlights++;
				CargoEntity cargo = getCargoByFlightId(flight.getFlightId(), cargoList);
				arrivingBaggagePieces += calculateTotalBaggagePieces(cargo);
			}
		}
		airportResponse.setNumberOfFlightsArriving(arrivingFlights);
		airportResponse.setNumberOfFlightsDeparting(departingFlights);
		airportResponse.setTotalPiecesOfBaggageArriving(arrivingBaggagePieces);
		airportResponse.setTotalPiecesOfBaggageDeparting(departingBaggagePieces);

		return airportResponse;
	}

	private CargoEntity getCargoByFlightId(int flightId, List<CargoEntity> cargoList) {
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

	public List<FlightEntity> getFlights() {
		return flights;
	}

}

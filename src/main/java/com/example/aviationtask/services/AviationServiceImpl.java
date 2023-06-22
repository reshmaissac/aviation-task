package com.example.aviationtask.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.aviationtask.dto.AirportResponse;
import com.example.aviationtask.dto.FlightEntityDto;
import com.example.aviationtask.dto.FlightResponse;
import com.example.aviationtask.model.Baggage;
import com.example.aviationtask.model.CargoItem;
import com.example.aviationtask.model.FlightEntity;
import com.example.aviationtask.repository.BaggageRepository;
import com.example.aviationtask.repository.CargoRepository;
import com.example.aviationtask.repository.FlightRepository;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class AviationServiceImpl implements AviationService {

	private final FlightRepository flightRepository;
	private final CargoRepository cargoRepository;
	private final BaggageRepository baggageRepository;

	/**
	 * Task 1.
	 */
	@Override
	public FlightResponse getFlightDetailsByNumbAndDate(int flightNumber, String departureDate) {
		FlightResponse flightResponse = new FlightResponse();
		FlightEntity requestedFlight = flightRepository.findByFlightNumberAndDepartureDate(flightNumber, departureDate);

		List<CargoItem> cargoList = cargoRepository.findByFlightId(requestedFlight.getId());
		int cargoWeight = calculateCargoWeight(cargoList);

		List<Baggage> baggageList = baggageRepository.findByFlightId(requestedFlight.getId());
		int baggageWeight = calculateBaggageWeight(baggageList);
		int totalWeight = cargoWeight + baggageWeight;

		FlightEntityDto reqflightEntityDto = new FlightEntityDto(requestedFlight.getFlightNumber(),
				requestedFlight.getDepartureDate(), requestedFlight.getDepartureAirportIATACode(),
				requestedFlight.getArrivalAirportIATACode());
		flightResponse.setFlight(reqflightEntityDto);
		flightResponse.setCargoWeight(cargoWeight);
		flightResponse.setBaggageWeight(baggageWeight);
		flightResponse.setTotalWeight(totalWeight);
		return flightResponse;
	}

	/**
	 * Task 2.
	 */
	@Override
	public AirportResponse getAirportDetailsByIATACodeAndDate(String airportCode, String departureDate) {

		AirportResponse airportResponse = new AirportResponse();

		int departingFlights = 0;
		int arrivingFlights = 0;
		int arrivingBaggagePieces = 0;
		int departingBaggagePieces = 0;

		List<FlightEntity> flightsList = flightRepository.findAll();
		for (FlightEntity flight : flightsList) {
			List<Baggage> baggageList = baggageRepository.findByFlightId(flight.getId());
			if (flight.getDepartureAirportIATACode().equals(airportCode)
					&& flight.getDepartureDate().contains(departureDate)) {
				departingFlights++;

				departingBaggagePieces += calculateTotalBaggagePieces(baggageList);
			}
			if (flight.getArrivalAirportIATACode().equals(airportCode)
					&& flight.getDepartureDate().contains(departureDate)) {
				arrivingFlights++;

				arrivingBaggagePieces += calculateTotalBaggagePieces(baggageList);
			}
		}
		airportResponse.setNumberOfFlightsArriving(arrivingFlights);
		airportResponse.setNumberOfFlightsDeparting(departingFlights);
		airportResponse.setTotalPiecesOfBaggageArriving(arrivingBaggagePieces);
		airportResponse.setTotalPiecesOfBaggageDeparting(departingBaggagePieces);

		return airportResponse;
	}

	/**
	 * Method to calculate total CargoWeight.
	 * 
	 * @param cargoList
	 * @return
	 */
	private int calculateCargoWeight(List<CargoItem> cargoList) {
		int cargoWeight = 0;
		for (CargoItem cargoItem : cargoList) {
			cargoWeight += cargoItem.getWeight();
		}
		return cargoWeight;
	}

	/**
	 * Method to calculate total BaggageWeight.
	 * 
	 * @param baggageList
	 * @return
	 */
	private int calculateBaggageWeight(List<Baggage> baggageList) {
		int baggageWeight = 0;
		for (Baggage baggage : baggageList) {
			baggageWeight += baggage.getWeight();
		}
		return baggageWeight;
	}

	/**
	 * Method to calculate Total BaggagePieces.
	 * 
	 * @param baggageList
	 * @return
	 */
	private int calculateTotalBaggagePieces(List<Baggage> baggageList) {
		int totalBaggagePieces = 0;
		for (Baggage baggage : baggageList) {
			totalBaggagePieces += baggage.getPieces();
		}
		return totalBaggagePieces;
	}

}

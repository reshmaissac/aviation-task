package com.example.aviationtask.services;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	// private static final Logger logger =
	// LogManager.getLogger(AviationServiceImpl.class);
	private static final Logger logger = LoggerFactory.getLogger(AviationServiceImpl.class);

	private final FlightRepository flightRepository;
	private final CargoRepository cargoRepository;
	private final BaggageRepository baggageRepository;

	/**
	 * Task 1.
	 */
	@Override
	public FlightResponse getFlightDetailsByNumbAndDate(int flightNumber, String departureDate) {

		FlightResponse flightResponse = new FlightResponse();
		try {
			FlightEntity requestedFlight = flightRepository.findByFlightNumberAndDepartureDate(flightNumber,
					departureDate);

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
		} catch (Exception e) {
			logger.error("Error occurred while fetching flight details: {}", e.getMessage());
		}
		return flightResponse;
	}

	/**
	 * Task 2.
	 */
	@Override
	public AirportResponse getAirportDetailsByIATACodeAndDate(String airportCode, String departureDate) {

		AirportResponse airportResponse = new AirportResponse();
		try {
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

		} catch (Exception e) {
			logger.error("Error occurred while fetching airport details: {}", e.getMessage());
		}

		return airportResponse;
	}

	/**
	 * Method to calculate total CargoWeight.
	 * 
	 * @param cargoList
	 * @return
	 */
	private int calculateCargoWeight(List<CargoItem> cargoList) {

		return cargoList.stream().mapToInt(CargoItem::getWeight).sum();
	}

	/**
	 * Method to calculate total BaggageWeight.
	 * 
	 * @param baggageList
	 * @return
	 */
	private int calculateBaggageWeight(List<Baggage> baggageList) {

		return baggageList.stream().mapToInt(Baggage::getWeight).sum();
	}

	/**
	 * Method to calculate Total BaggagePieces.
	 * 
	 * @param baggageList
	 * @return
	 */
	private int calculateTotalBaggagePieces(List<Baggage> baggageList) {

		return baggageList.stream().mapToInt(Baggage::getPieces).sum();
	}

}

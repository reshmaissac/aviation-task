package com.example.aviationtask.services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.example.aviationtask.dto.FlightCargoDto;
import com.example.aviationtask.dto.FlightEntityDto;
import com.example.aviationtask.model.Baggage;
import com.example.aviationtask.model.CargoItem;
import com.example.aviationtask.model.FlightEntity;
import com.example.aviationtask.repository.BaggageRepository;
import com.example.aviationtask.repository.CargoRepository;
import com.example.aviationtask.repository.FlightRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class DataLoadService {

	Logger logger = LoggerFactory.getLogger(DataLoadService.class); 

	private final FlightRepository flightRepository;
	private final BaggageRepository baggageRespository;
	private final CargoRepository cargoRepository;

	private List<FlightEntityDto> flights;
	private List<FlightCargoDto> cargoList;

	@Autowired
	private ObjectMapper objectMapper;

	private String flightsJsonPath;
	private String cargoJsonPath;

	@Autowired
	public DataLoadService(ObjectMapper objectMapper, @Value("${flights.json.path}") String flightsJsonPath,
			@Value("${cargo.json.path}") String cargoJsonPath, FlightRepository flightRepository,
			BaggageRepository baggageRepository, CargoRepository cargoRepository) {
		this.objectMapper = objectMapper;
		this.flightsJsonPath = flightsJsonPath; // filepath from application.properties
		this.cargoJsonPath = cargoJsonPath; // filepath from application.properties
		this.flightRepository = flightRepository;
		this.baggageRespository = baggageRepository;
		this.cargoRepository = cargoRepository;
	}

	/**
	 * Method to load data from JSON file.
	 */
	@PostConstruct
	private void loadDataFromJson() {
		try {
			// Read flights.json file
			ClassPathResource flightsResource = new ClassPathResource(flightsJsonPath);
			byte[] flightsJsonBytes = FileCopyUtils.copyToByteArray(flightsResource.getInputStream());
			String flightsJson = new String(flightsJsonBytes, StandardCharsets.UTF_8);
			flights = objectMapper.readValue(flightsJson, new TypeReference<List<FlightEntityDto>>() {
			});
			flights.forEach(this::save);

			// Read cargo.json file
			ClassPathResource cargoResource = new ClassPathResource(cargoJsonPath);
			byte[] cargoJsonBytes = FileCopyUtils.copyToByteArray(cargoResource.getInputStream());
			String cargoJson = new String(cargoJsonBytes, StandardCharsets.UTF_8);
			cargoList = objectMapper.readValue(cargoJson, new TypeReference<List<FlightCargoDto>>() {
			});
			cargoList.forEach(this::save);

			logger.info("Data Loaded from json files!!");
		} catch (IOException e) {
			logger.error("Error occurred while loading data from json files: {}", e.getMessage());
			//e.printStackTrace();
			flights = new ArrayList<>();
			cargoList = new ArrayList<>();
		}
	}

	public void save(FlightEntityDto flightDTO) {
		FlightEntity flight = new FlightEntity(flightDTO.getFlightId() + 1, flightDTO.getFlightNumber(),
				flightDTO.getDepartureAirportIATACode(), flightDTO.getArrivalAirportIATACode(),
				flightDTO.getDepartureDate());
		flightRepository.save(flight);
	}

	public void save(FlightCargoDto flightCargoDTO) {
		FlightEntity flight = flightRepository.getReferenceById((long) (flightCargoDTO.getFlightId() + 1));

		List<Baggage> baggageList = flightCargoDTO.getBaggage().stream()
				.map(baggage -> new Baggage(baggage.getWeight(), baggage.getWeightUnit(), baggage.getPieces(), flight))
				.collect(Collectors.toList());
		baggageRespository.saveAll(baggageList);

		List<CargoItem> cargoList = flightCargoDTO.getCargo().stream()
				.map(cargo -> new CargoItem(cargo.getWeight(), cargo.getWeightUnit(), cargo.getPieces(), flight))
				.collect(Collectors.toList());
		cargoRepository.saveAll(cargoList);
	}

	public List<FlightEntityDto> getFlights() {
		return flights;
	}

}

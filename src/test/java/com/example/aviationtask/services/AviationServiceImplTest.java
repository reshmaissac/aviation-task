package com.example.aviationtask.services;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.aviationtask.dto.AirportResponse;
import com.example.aviationtask.dto.FlightResponse;
import com.example.aviationtask.model.Baggage;
import com.example.aviationtask.model.CargoItem;
import com.example.aviationtask.model.FlightEntity;
import com.example.aviationtask.repository.BaggageRepository;
import com.example.aviationtask.repository.CargoRepository;
import com.example.aviationtask.repository.FlightRepository;

public class AviationServiceImplTest {

    @Mock
    private FlightRepository flightRepository;
    
    @Mock
    private CargoRepository cargoRepository;
    
    @Mock
    private BaggageRepository baggageRepository;

    private AviationService aviationService;

    @BeforeEach
    public void setUp() {
    	MockitoAnnotations.openMocks(this);
        aviationService = new AviationServiceImpl(flightRepository, cargoRepository, baggageRepository);
    }

    @Test
    public void testGetFlightDetailsByNumbAndDate() {
        // Prepare test data
        int flightNumber = 123;
        String departureDate = "2022-01-01";

        FlightEntity flightEntity = createFlightEntity(1L, flightNumber, "ABC", "XYZ", departureDate);

        List<CargoItem> cargoList = new ArrayList<>();
        cargoList.add(new CargoItem(100, "KG", 2,flightEntity));
        cargoList.add(new CargoItem(200, "KG",2, flightEntity));

        List<Baggage> baggageList = new ArrayList<>();
        baggageList.add(new Baggage(10, "KG", 2,flightEntity));
        baggageList.add(new Baggage(20, "KG", 2,flightEntity));

        when(flightRepository.findByFlightNumberAndDepartureDate(flightNumber, departureDate))
                .thenReturn(flightEntity);
        when(cargoRepository.findByFlightId(flightEntity.getId())).thenReturn(cargoList);
        when(baggageRepository.findByFlightId(flightEntity.getId())).thenReturn(baggageList);

        // Execute the method
        FlightResponse flightResponse = aviationService.getFlightDetailsByNumbAndDate(flightNumber, departureDate);

        // Verify the results
        assertEquals(flightNumber, flightResponse.getFlight().getFlightNumber());
        assertEquals(departureDate, flightResponse.getFlight().getDepartureDate());
        assertEquals("ABC", flightResponse.getFlight().getDepartureAirportIATACode());
        assertEquals("XYZ", flightResponse.getFlight().getArrivalAirportIATACode());
        assertEquals(300, flightResponse.getCargoWeight());
        assertEquals(30, flightResponse.getBaggageWeight());
        assertEquals(330, flightResponse.getTotalWeight());

        
    }

    @Test
    public void testGetAirportDetailsByIATACodeAndDate() {
        // Prepare test data
        String airportCode = "ABC";
        String departureDate = "2022-01-01";

        List<FlightEntity> flightsList = new ArrayList<>();
        FlightEntity flightEntity1 = createFlightEntity(1L, 123, airportCode, "XYZ", departureDate);
		flightsList.add(flightEntity1);
        FlightEntity flightEntity2 = createFlightEntity(2L, 123, "DEF", airportCode, departureDate);
		flightsList.add(flightEntity2);
        flightsList.add(createFlightEntity(3L,123,"GHI" ,airportCode, departureDate));

        List<Baggage> baggageList = new ArrayList<>();
        baggageList.add(new Baggage(10, "KG", 2,flightEntity1));
        baggageList.add(new Baggage(10, "KG", 2,flightEntity2));

        when(flightRepository.findAll()).thenReturn(flightsList);
        when(baggageRepository.findByFlightId(anyLong())).thenReturn(baggageList);

        // Execute the method
        AirportResponse airportResponse = aviationService.getAirportDetailsByIATACodeAndDate(airportCode, departureDate);

        // Verify the results
        assertEquals(2, airportResponse.getNumberOfFlightsArriving());
        assertEquals(1, airportResponse.getNumberOfFlightsDeparting());
        assertEquals(8, airportResponse.getTotalPiecesOfBaggageArriving());
        assertEquals(4, airportResponse.getTotalPiecesOfBaggageDeparting());

        
    }

    private FlightEntity createFlightEntity(Long id, int flightNumber, String departureAirportCode, String arrivalAirportCode, String departureDate) {
        FlightEntity flightEntity = new FlightEntity();
        flightEntity.setId(id);
        flightEntity.setFlightNumber(flightNumber);
        flightEntity.setDepartureAirportIATACode(departureAirportCode);
        flightEntity.setArrivalAirportIATACode(arrivalAirportCode);
        flightEntity.setDepartureDate(departureDate);
        return flightEntity;
    }
}

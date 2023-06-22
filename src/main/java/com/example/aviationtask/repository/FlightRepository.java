package com.example.aviationtask.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.aviationtask.model.FlightEntity;

public interface FlightRepository extends JpaRepository<FlightEntity, Long> {

	FlightEntity findByFlightNumberAndDepartureDate(int flightNumber, String departureDate);
}

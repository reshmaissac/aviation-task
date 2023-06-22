package com.example.aviationtask.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.aviationtask.model.CargoItem;

public interface CargoRepository extends JpaRepository<CargoItem, Long> {

	List<CargoItem> findByFlightId(long flightId);

}

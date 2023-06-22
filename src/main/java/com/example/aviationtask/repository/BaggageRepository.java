package com.example.aviationtask.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.aviationtask.model.Baggage;

public interface BaggageRepository extends JpaRepository<Baggage, Long> {

	List<Baggage> findByFlightId(long id);

}

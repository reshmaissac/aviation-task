package com.example.aviationtask.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class CargoItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private int weight;
	private String weightUnit;
	private int pieces;

	@ManyToOne
	@JoinColumn(name = "flight_id")
	private FlightEntity flight;

	public CargoItem(int weight, String weightUnit, int pieces, FlightEntity flight) {
		this.weight = weight;
		this.weightUnit = weightUnit;
		this.pieces = pieces;
		this.flight = flight;
	}
}
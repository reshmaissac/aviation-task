package com.example.aviationtask.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class CargoEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private long flightId;
	@OneToMany(mappedBy = "flight", cascade = CascadeType.ALL)
	private List<Baggage> baggage;
	@OneToMany(mappedBy = "flight", cascade = CascadeType.ALL)
	private List<CargoItem> cargo;

}

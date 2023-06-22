package com.example.aviationtask.dto;

import lombok.Data;

@Data
public class CargoDto {
	private int id;
	private int weight;
	private String weightUnit;
	private int pieces;
}
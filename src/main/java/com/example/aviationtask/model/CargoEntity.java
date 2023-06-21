package com.example.aviationtask.model;

import java.util.List;

import lombok.Data;

@Data
public class CargoEntity {

    private int flightId;
    private List<Baggage> baggage;
    private List<Cargo> cargo;
}





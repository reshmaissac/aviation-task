package com.example.aviationtask.model;

import lombok.Data;

@Data
public class Cargo {
    private int id;
    private int weight;
    private String weightUnit;
    private int pieces;
}
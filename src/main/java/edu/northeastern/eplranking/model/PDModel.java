package edu.northeastern.eplranking.model;

import lombok.Data;

import java.util.Map;

@Data
public class PDModel {
    private Team team1;
    private Team team2;


    private Map<Integer, Double> probabilityDistribution;
}

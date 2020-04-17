package edu.northeastern.eplranking.model;

import lombok.Data;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

@Data
public class ProbDensity {
    private double mean;
    private double standardDeviation;
    private double variance;

    private Map<Integer, Double> goalDiffProbabilityMap = new TreeMap<>(Comparator.comparingInt(Integer::intValue));

}

package edu.northeastern.eplranking.model;

import lombok.Data;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Data
public class Stats {
    private int wins;
    private int losses;
    private int draws;

    //instance holding team's win count as a home team against other team
    private int homeWins;
    private int homeLosses;
    private int homeDraws;


    private ProbDensity probDensity = new ProbDensity();

    private SortedMap<Integer, Map<Integer, Integer>> goalDiffFrequencyMap = new TreeMap<>(Comparator.comparingInt(Integer::intValue));

    public int getTotalMatches() {
        return wins + losses + draws;
    }
}

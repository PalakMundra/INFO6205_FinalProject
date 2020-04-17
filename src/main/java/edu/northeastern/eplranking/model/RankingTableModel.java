package edu.northeastern.eplranking.model;

import lombok.Getter;
import lombok.Setter;


/*
* 1. Seasonal Weightage If less than 2 ma
* 2. Overall Weightage
* 3.
* */
@Getter
@Setter
public class RankingTableModel {
    private Team team;
    private int rank;
    private double winProbability;

}

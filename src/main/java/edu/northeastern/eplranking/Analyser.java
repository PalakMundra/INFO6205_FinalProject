package edu.northeastern.eplranking;

import edu.northeastern.eplranking.model.*;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.IterativeLegendreGaussIntegrator;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Main analysis class where PDFs are generated and all predictions are calculated
 */
public class Analyser {

    private final Context app;

    public Analyser() {
        this.app = Context.getInstance();
    }

    /**
     * Consolidate Match Statistics and Build Team wise probability distribution functions
     */
    public void buildPdf() {
        List<MatchResult> matchResults = app.getMatchResults();
        Map<Team, List<MatchResult>> matchResultMap = app.getMatchResultMap();
        List<Team> teamList = app.getTeamList();

        //Building a simple map of teams vs all match results for that team
        for (MatchResult matchResult : matchResults) {
            if (matchResultMap.containsKey(matchResult.getHomeTeam())) {
                matchResultMap.get(matchResult.getHomeTeam()).add(matchResult);
            } else {
                List<MatchResult> list = new ArrayList<>();
                list.add(matchResult);
                matchResultMap.put(matchResult.getHomeTeam(), list);
            }
            if (matchResultMap.containsKey(matchResult.getAwayTeam())) {
                matchResultMap.get(matchResult.getAwayTeam()).add(matchResult);
            } else {
                List<MatchResult> list = new ArrayList<>();
                list.add(matchResult);
                matchResultMap.put(matchResult.getAwayTeam(), list);
            }

        }

        app.setMaxScoreDifference(matchResults.stream()
                .map(matchResult -> Math.abs(matchResult.getFullTimeHomeScore() - matchResult.getFullTimeAwayScore()))
                .max(Comparator.comparingInt(Integer::intValue))
                .get());


        for (Team team : teamList) {
            TeamStats teamStats = new TeamStats();
            teamStats.setTeam(team);
            teamStats.setMatchResultList(matchResultMap.get(team));


            teamList.stream().filter(otherTeam -> !otherTeam.equals(team)).forEach(otherTeam -> {
                Stats stats = new Stats();
                stats.setWins(0);
                stats.setLosses(0);
                stats.setDraws(0);
                stats.setHomeWins(0);
                stats.setHomeDraws(0);
                stats.setHomeLosses(0);

                teamStats.getMatchResultList().stream()
                        .filter(matchResult -> matchResult.getStatus().equalsIgnoreCase("ft") && List.of(matchResult.getHomeTeam(), matchResult.getAwayTeam()).contains(otherTeam))
                        .forEach(matchResult -> {
                            int scoreDiff = matchResult.getFullTimeGoalDifference();
                            if (matchResult.getHomeTeam().equals(team)) {
                                if (scoreDiff > 0) {
                                    stats.setHomeWins(stats.getHomeWins() + 1);
                                } else if (scoreDiff < 0) {
                                    stats.setHomeLosses(stats.getHomeLosses() + 1);
                                } else {
                                    stats.setHomeDraws(stats.getHomeDraws() + 1);
                                }

                            } else {
                                scoreDiff = -1 * scoreDiff;
                            }

                            if (scoreDiff > 0) {
                                stats.setWins(stats.getWins() + 1);
                            } else if (scoreDiff < 0) {
                                stats.setLosses(stats.getLosses() + 1);
                            } else {
                                stats.setDraws(stats.getDraws() + 1);
                            }

                            //Goal diff vs season wise frequency
                            Map<Integer, Map<Integer, Integer>> goalDiffFrequencyMap = stats.getGoalDiffFrequencyMap();

                            if (goalDiffFrequencyMap.containsKey(scoreDiff)) {
                                Map<Integer, Integer> val = goalDiffFrequencyMap.get(scoreDiff);
                                if (val.containsKey(matchResult.getSeason())) {
                                    val.put(matchResult.getSeason(), val.get(matchResult.getSeason()) + 1);
                                } else {
                                    val.put(matchResult.getSeason(), 1);
                                }
                            } else {
                                goalDiffFrequencyMap.put(scoreDiff, new HashMap<>(Map.of(matchResult.getSeason(), 1)));
                            }
                        });

                ProbDensity probDensity = stats.getProbDensity();
                Map<Integer, Double> map = stats.getGoalDiffFrequencyMap().entrySet().stream().collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> {
                            double probability = 0;
                            for (Map.Entry<Integer, Integer> entry : e.getValue().entrySet()) {
                                probability += (1 - ((2019 - entry.getKey()) * 0.1)) * entry.getValue() / e.getValue().values().stream().mapToInt(Integer::intValue).sum();
                            }
                            //(double) e.getValue() / stats.getTotalMatches();
                            return probability;
                        }, (v1, v2) -> {
                            throw new IllegalStateException();
                        }, TreeMap::new
                ));
                probDensity.setGoalDiffProbabilityMap(map);

                probDensity.setMean(probDensity.getGoalDiffProbabilityMap().entrySet().stream().map(e -> e.getKey() * e.getValue()).mapToDouble(Double::doubleValue).sum());
                probDensity.setVariance(probDensity.getGoalDiffProbabilityMap().entrySet().stream().map(p1 -> Math.pow(p1.getKey() - probDensity.getMean(), 2) * p1.getValue()).mapToDouble(Double::valueOf).sum());
                probDensity.setStandardDeviation(Math.sqrt(probDensity.getVariance()));

                teamStats.getStatsMapVsX().put(otherTeam, stats);
            });
            app.getTeamStats().add(teamStats);


        }
    }

    /**
     * Returns probability distribution of goals scored by Team1 vs Team2 (Team1-Team2)
     *
     * @param team1 {@link Team}
     * @param team2 {@link Team}
     * @return {@link ProbDensity}
     */
    public ProbDensity p(Team team1, Team team2) {
        return app.getTeamStats().stream()
                .filter(teamStats -> teamStats.getTeam().equals(team1))
                .findAny().get().getStatsMapVsX().get(team2).getProbDensity();
    }

    /**
     * Builds a standings table on the basis of matches which were completed as part of the 2019 EPL season
     */
    public void build2019Standings() {
        List<StandingTableModel> standingTable = new ArrayList<>();
        for (MatchResult matchResult : app.getMatchResults()) {
            if (matchResult.getSeason() == 2019) {
                if (matchResult.getStatus().equalsIgnoreCase("ft")) {
                    StandingTableModel homeTeamModel;
                    Optional<StandingTableModel> model = standingTable.stream().filter(standingTableModel -> standingTableModel.getTeam().equals(matchResult.getHomeTeam()))
                            .findAny();
                    if (model.isEmpty()) {
                        homeTeamModel = new StandingTableModel();
                        homeTeamModel.setTeam(matchResult.getHomeTeam());
                        standingTable.add(homeTeamModel);
                    } else {
                        homeTeamModel = model.get();
                    }

                    if (matchResult.getFullTimeGoalDifference() > 0) {
                        homeTeamModel.setWins(homeTeamModel.getWins() + 1);
                    } else if (matchResult.getFullTimeGoalDifference() < 0) {
                        homeTeamModel.setLosses(homeTeamModel.getLosses() + 1);
                    } else {
                        homeTeamModel.setDraws(homeTeamModel.getDraws() + 1);
                    }

                    homeTeamModel.setGoalsFor(homeTeamModel.getGoalsFor() + matchResult.getFullTimeHomeScore());
                    homeTeamModel.setGoalsAgainst(homeTeamModel.getGoalsAgainst() + matchResult.getFullTimeAwayScore());

                    StandingTableModel awayTeamModel;
                    model = standingTable.stream().filter(standingTableModel -> standingTableModel.getTeam().equals(matchResult.getAwayTeam()))
                            .findAny();
                    if (model.isEmpty()) {
                        awayTeamModel = new StandingTableModel();
                        awayTeamModel.setTeam(matchResult.getAwayTeam());
                        standingTable.add(awayTeamModel);
                    } else {
                        awayTeamModel = model.get();
                    }

                    if (matchResult.getFullTimeGoalDifference() < 0) {
                        awayTeamModel.setWins(awayTeamModel.getWins() + 1);
                    } else if (matchResult.getFullTimeGoalDifference() > 0) {
                        awayTeamModel.setLosses(awayTeamModel.getLosses() + 1);
                    } else {
                        awayTeamModel.setDraws(awayTeamModel.getDraws() + 1);
                    }

                    awayTeamModel.setGoalsFor(awayTeamModel.getGoalsFor() + matchResult.getFullTimeAwayScore());
                    awayTeamModel.setGoalsAgainst(awayTeamModel.getGoalsAgainst() + matchResult.getFullTimeHomeScore());

                }
            }
        }
        Collections.sort(standingTable);
        app.setCurrent2019standings(standingTable);
        //return standingTable;
    }

    /**
     * Builds a final standings table (along with number of goals predictions) on the basis of predictions for rest of the matches which are yet to take place
     */
    public List<StandingTableModel> predict2019Standings() {
        Random random = new Random();
        List<StandingTableModel> standingsTable = new ArrayList<>();
        app.getCurrent2019standings().forEach(currentStanding -> standingsTable.add(new StandingTableModel(currentStanding)));
        List<MatchResult> simulated2019Results = app.getSimulated2019Results();
        for (MatchResult matchResult : app.getMatchResults()) {
            if (matchResult.getSeason() == 2019 && matchResult.getStatus().equalsIgnoreCase("ns")) {
                ProbDensity probDensity = p(matchResult.getHomeTeam(), matchResult.getAwayTeam());
                int predictedGoalDifference = ((Long) Math.round(probDensity.getMean())).intValue();
                int predictedHomeScore;
                int predictedAwayScore;
                StandingTableModel homeModel = standingsTable.stream().filter(model -> model.getTeam().equals(matchResult.getHomeTeam())).findAny().get();
                StandingTableModel awayModel = standingsTable.stream().filter(model -> model.getTeam().equals(matchResult.getAwayTeam())).findAny().get();
                if (predictedGoalDifference > 0) {
                    predictedGoalDifference = Math.min(predictedGoalDifference, 5);
                    predictedAwayScore = random.nextInt(3);
                    predictedHomeScore = predictedAwayScore + predictedGoalDifference;
                    homeModel.setWins(homeModel.getWins() + 1);
                    awayModel.setLosses(awayModel.getLosses() + 1);
                } else if (predictedGoalDifference < 0) {
                    predictedGoalDifference = Math.max(predictedGoalDifference, -5);
                    predictedHomeScore = random.nextInt(3);
                    predictedAwayScore = predictedHomeScore + Math.abs(predictedGoalDifference);
                    awayModel.setWins(awayModel.getWins() + 1);
                    homeModel.setLosses(homeModel.getLosses() + 1);
                } else {
                    predictedAwayScore = random.nextInt(3);
                    predictedHomeScore = predictedAwayScore;
                    homeModel.setDraws(homeModel.getDraws() + 1);
                    awayModel.setDraws(awayModel.getDraws() + 1);
                }
                homeModel.setGoalsFor(homeModel.getGoalsFor() + predictedHomeScore);
                homeModel.setGoalsAgainst(homeModel.getGoalsAgainst() + predictedAwayScore);
                awayModel.setGoalsFor(awayModel.getGoalsFor() + predictedAwayScore);
                awayModel.setGoalsAgainst(awayModel.getGoalsAgainst() + predictedHomeScore);

                matchResult.setFullTimeHomeScore(predictedHomeScore);
                matchResult.setFullTimeAwayScore(predictedAwayScore);

                app.getConsolidatedProbDensity2019().setMean(app.getConsolidatedProbDensity2019().getMean() + probDensity.getMean());
                app.getConsolidatedProbDensity2019().setVariance(app.getConsolidatedProbDensity2019().getVariance() + probDensity.getVariance());
                //matchResult.setProbability(pr);
                simulated2019Results.add(matchResult);
            }
        }
        Collections.sort(standingsTable);
        return standingsTable;

    }

    /**
     * Consolidate All pdfs and generate a cumulative ranking
     */
    public void generateOverallRankings() {
        Set<RankingTableModel> rankingTable = app.getRankingTable();
        List<TeamStats> teamStatsList = app.getTeamStats();
        teamStatsList.forEach(teamStats -> {
            RankingTableModel rankingTableModel = new RankingTableModel();
            rankingTableModel.setTeam(teamStats.getTeam());

            double totalMean = teamStats.getStatsMapVsX().values().stream().map(stats -> stats.getProbDensity().getMean()).mapToDouble(Double::doubleValue).sum();
            double totalVariance = teamStats.getStatsMapVsX().values().stream().map(stats -> stats.getProbDensity().getVariance()).mapToDouble(Double::doubleValue).sum();
            double totalStandardDeviation = Math.sqrt(totalVariance);
            rankingTableModel.setWinProbability(getAreaUnderTheCurve(totalMean, totalVariance, totalStandardDeviation, 0.1, 200));
            rankingTable.add(rankingTableModel);
        });

    }

    /**
     * Probability Density Function for a normal distribution
     *
     * @param mean
     * @param variance
     * @param standardDeviation
     * @return {@link UnivariateFunction}
     */
    public UnivariateFunction getProbabilityDistributionFunction(double mean, double variance, double standardDeviation) {
        return x -> (Math.pow(Math.E, -1 * Math.pow(x - mean, 2) / (2 * variance))) / (standardDeviation * Math.sqrt(2 * Math.PI));
    }

    /**
     * Get Area Under the curve (For calculating cumulative probabilities)
     *
     * @param mean
     * @param variance
     * @param standardDeviation
     * @param start
     * @param end
     * @return double probability
     */
    public double getAreaUnderTheCurve(double mean, double variance, double standardDeviation, double start, double end) {
        UnivariateFunction f = getProbabilityDistributionFunction(mean, variance, standardDeviation);
        return new IterativeLegendreGaussIntegrator(5, 1.0e-9, 1.0e-8, 2, 15).integrate(10000000, f, start, end);
    }
}

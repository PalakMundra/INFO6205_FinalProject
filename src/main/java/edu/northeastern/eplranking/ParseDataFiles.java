package edu.northeastern.eplranking;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.eplranking.model.MatchResult;
import edu.northeastern.eplranking.model.Team;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Loads data from resources/data into application shared context ({@link Context})
 */
public class ParseDataFiles {
    public void parse() throws IOException, URISyntaxException {
        ObjectMapper mapper = new ObjectMapper();
        File dataDirectory = new File(getClass().getClassLoader().getResource("data").toURI());
        Context app = Context.getInstance();
        List<Team> teamList = app.getTeamList();

        for (File file : dataDirectory.listFiles()) {
            Map map = mapper.readValue(file, Map.class);

            app.getMatchResults().addAll(((List<Map<String, Map>>) map.get("response")).stream().map(item -> {
                Map<String, Map<String, String>> teams = item.get("teams");

                String homeTeamName = teams.get("home").get("name");
                String homeTeamlogo = teams.get("home").get("logo");
                Team homeTeam = teamList.stream().filter(team -> team.getName().equals(homeTeamName)).findAny().orElse(null);
                if (homeTeam == null) {
                    homeTeam = new Team(homeTeamName, homeTeamlogo);
                    teamList.add(homeTeam);
                }

                String awayTeamName = teams.get("away").get("name");
                String awayTeamlogo = teams.get("away").get("logo");
                Team awayTeam = teamList.stream().filter(team -> team.getName().equals(awayTeamName)).findAny().orElse(null);
                if (awayTeam == null) {
                    awayTeam = new Team(awayTeamName, awayTeamlogo);
                    teamList.add(awayTeam);
                }

                Map<String, Object> fixture = item.get("fixture");
                Map<String, Map<String, Integer>> score = item.get("score");

                MatchResult matchResult = new MatchResult();
                matchResult.setSeason((int) item.get("league").get("season"));
                matchResult.setStatus(((Map<String, String>) fixture.get("status")).get("short"));
                matchResult.setHomeTeam(homeTeam);
                matchResult.setAwayTeam(awayTeam);
                matchResult.setMatchDateTime(LocalDateTime.ofInstant(Instant.ofEpochSecond((Integer) fixture.get("timestamp")), ZoneId.of((String) fixture.get("timezone"))));

                if (matchResult.getStatus().equalsIgnoreCase("ft")) {
                    matchResult.setFullTimeHomeScore(score.get("fulltime").get("home"));
                    matchResult.setFullTimeAwayScore(score.get("fulltime").get("away"));
                    matchResult.setHalfTimeHomeScore(score.get("halftime").get("home"));
                    matchResult.setHalfTimeAwayScore(score.get("halftime").get("away"));
                }
                return matchResult;
            }).collect(Collectors.toList()));
        }


        teamList.sort(Comparator.comparing(Team::getName));
        app.getMatchResults().sort(Comparator.comparing(MatchResult::getMatchDateTime));

    }

    /*public void parseold() throws IOException, URISyntaxException {
        ObjectMapper mapper = new ObjectMapper();
        File dataDirectory = new File(getClass().getClassLoader().getResource("data").toURI());
        GameData gameData = GameData.getInstance();
        List<Team> teamList = gameData.getTeamList();

        for (File file : dataDirectory.listFiles()) {
            List<Map<String, Object>> list = mapper.readValue(file, List.class);

            gameData.getMatchResults().addAll(list.stream().map(item -> {
                Team homeTeam = teamList.stream().filter(team -> team.getName().equals(item.get("HomeTeam"))).findAny().orElse(null);
                if (homeTeam == null) {
                    homeTeam = new Team((String) item.get("HomeTeam"));
                    teamList.add(homeTeam);
                }
                Team awayTeam = teamList.stream().filter(team -> team.getName().equals(item.get("AwayTeam"))).findAny().orElse(null);
                if (awayTeam == null) {
                    awayTeam = new Team((String) item.get("AwayTeam"));
                    teamList.add(awayTeam);
                }

                MatchResult matchResult = new MatchResult();
                matchResult.setHomeTeam(homeTeam);
                matchResult.setAwayTeam(awayTeam);
                //Extremely dirty quick hack
                try {
                    matchResult.setMatchDate(LocalDate.parse((String) item.get("Date"), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                } catch (DateTimeParseException e) {
                    try {
                        matchResult.setMatchDate(LocalDate.parse((String) item.get("Date"), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    } catch (DateTimeParseException ex) {
                        matchResult.setMatchDate(LocalDate.parse((String) item.get("Date"), DateTimeFormatter.ofPattern("dd/MM/yy")));
                    }

                }
                matchResult.setFullTimeAwayScore((int) item.get("FTAG"));
                matchResult.setFullTimeHomeScore((int) item.get("FTHG"));
                matchResult.setHalfTimeAwayScore((int) item.get("HTAG"));
                matchResult.setHalfTimeHomeScore((int) item.get("HTHG"));
                return matchResult;
            }).collect(Collectors.toList()));
        }

        teamList.sort(Comparator.comparing(Team::getName));
        gameData.getMatchResults().sort(Comparator.comparing(MatchResult::getMatchDate));

    }*/
}

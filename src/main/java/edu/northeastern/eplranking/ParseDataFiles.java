package edu.northeastern.eplranking;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.eplranking.model.MatchResult;
import edu.northeastern.eplranking.model.Team;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ParseDataFiles {
    public void parse() throws IOException, URISyntaxException {
        ObjectMapper mapper = new ObjectMapper();
        File dataDirectory = new File(getClass().getClassLoader().getResource("data").toURI());
        for (File file : dataDirectory.listFiles()) {
            List<Map<String, Object>> list = mapper.readValue(file, List.class);
            GameData gameData = GameData.getInstance();
            List<Team> teamList = gameData.getTeamList();

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
                    } catch (DateTimeParseException ex){
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

    }
}

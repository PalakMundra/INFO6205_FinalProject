package edu.northeastern.eplranking;

import edu.northeastern.eplranking.model.StandingTableModel;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnalyserTest {
    @Test
    public void testParsing() throws IOException, URISyntaxException {
        ParseDataFiles parseDataFiles = new ParseDataFiles();
        parseDataFiles.parse();
        assertNotNull(Context.getInstance().getTeamList());
        assertFalse(Context.getInstance().getTeamList().isEmpty());
        assertNotNull(Context.getInstance().getMatchResults());
        assertFalse(Context.getInstance().getMatchResults().isEmpty());
    }

    @Test
    public void test2019Standings() throws IOException, URISyntaxException {
        ParseDataFiles parseDataFiles = new ParseDataFiles();
        parseDataFiles.parse();
        Analyser analyser = new Analyser();
        analyser.build2019Standings();
        analyser.buildPdf();
        List<StandingTableModel> predicted = analyser.predict2019Standings();
        assertNotNull(predicted);
        assertEquals("Liverpool", predicted.get(0).getTeam().getName());
    }

}
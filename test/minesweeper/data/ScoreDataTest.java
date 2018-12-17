package minesweeper.data;

import minesweeper.model.Level;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class ScoreDataTest {

    private ScoreData data;
    private Score score1;
    private Score score2;
    private Score score3;
    private Level level;

    @BeforeEach
    void setUp() {
        data = ScoreData.getInstance();
        score1 = new Score("Ada", 6);
        score2 = new Score("Kate", 10);
        score3 = new Score("Joe", 12);
        level = Level.EASY;
    }

    @AfterEach
    void cleanUp() {
        data.clearAllScores(level);
    }

    @Test
    void shouldAllowToAddScoreToScoreData() {
        data.addScore(score1, level);

        assertThat(data.getScores(level), hasItem(score1));
    }

    @Test
    void shouldAllowToRemoveAllScoresFromScoreData() {
        data.addScore(score1, level);
        data.addScore(score2, level);
        data.clearAllScores(level);

        assertThat(data.getScores(level), hasSize(0));
    }

    @Test
    void shouldStoreScoresInAscendingOrder() {
        data.addScore(score2, level);
        data.addScore(score1, level);
        data.addScore(score3, level);

        List<Score> expected = createData(score1, score2, score3);

        assertThat(data.getScores(level), is(expected));
    }

    @Test
    void shouldAcceptScoreAsOneOfTheBestWhenListIsFull() {
        Score score4 = new Score("Ben", 7);
        Score score5 = new Score("Jane", 8);
        Score score6 = new Score("Rose", 9);
        Score score7 = new Score("Fiona", 10);
        Score score8 = new Score("Phoebe", 11);
        Score score9 = new Score("Eden", 12);
        Score score10 = new Score("Terese", 13);
        Score score11 = new Score("Edward", 6);

        data.addScore(score1, level);
        data.addScore(score2, level);
        data.addScore(score3, level);
        data.addScore(score4, level);
        data.addScore(score5, level);
        data.addScore(score6, level);
        data.addScore(score7, level);
        data.addScore(score8, level);
        data.addScore(score9, level);
        data.addScore(score10, level);

        assertThat(data.isBestScore(score11.getTime(), level), is(true));
    }

    @Test
    void shouldntAllowToStoreMoreThanTenBestScores() {
        Score score4 = new Score("Ben", 7);
        Score score5 = new Score("Jane", 8);
        Score score6 = new Score("Rose", 9);
        Score score7 = new Score("Fiona", 10);
        Score score8 = new Score("Phoebe", 11);
        Score score9 = new Score("Eden", 12);
        Score score10 = new Score("Terese", 13);
        Score score11 = new Score("Edward", 6);

        data.addScore(score2, level);
        data.addScore(score1, level);
        data.addScore(score3, level);
        data.addScore(score4, level);
        data.addScore(score5, level);
        data.addScore(score6, level);
        data.addScore(score7, level);
        data.addScore(score8, level);
        data.addScore(score9, level);
        data.addScore(score10, level);
        data.addScore(score11, level);

        List<Score> expected = createData(score1, score11, score4, score5,
                score6, score2, score7, score8, score3, score9);

        assertThat(data.getScores(level), is(expected));
    }

    private static List<Score> createData(Score... listContent) {
        return Arrays.asList(listContent);
    }
}
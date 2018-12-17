package minesweeper.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import minesweeper.model.Level;

import java.io.IOException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ScoreData {

    private static final int SCORES_SIZE = 10;
    private static final ScoreData INSTANCE = new ScoreData();
    private final String fileNameForEasyLevel = "bestscoreseasy.txt";
    private final String fileNameForMediumLevel = "bestscoresmedium.txt";
    private final String fileNameForHardLevel = "bestscoreshard.txt";
    private ObservableList<Score> scoresEasy = FXCollections.observableArrayList();
    private ObservableList<Score> scoresMedium = FXCollections.observableArrayList();
    private ObservableList<Score> scoresHard = FXCollections.observableArrayList();

    private ScoreData() {
        if (INSTANCE != null) {
            throw new IllegalStateException("Cannot create new instance.");
        }
    }

    public static ScoreData getInstance() {
        return INSTANCE;
    }

    public void addScore(Score score, Level level) {

        switch (level) {
            case EASY:
                if (scoresEasy.size() == SCORES_SIZE) {
                    scoresEasy.remove(SCORES_SIZE - 1);
                }
                scoresEasy.add(score);
                Collections.sort(scoresEasy);
                break;
            case MEDIUM:
                if (scoresMedium.size() == SCORES_SIZE) {
                    scoresMedium.remove(SCORES_SIZE - 1);
                }
                scoresMedium.add(score);
                Collections.sort(scoresMedium);
                break;
            case HARD:
                if (scoresHard.size() == SCORES_SIZE) {
                    scoresHard.remove(SCORES_SIZE - 1);
                }
                scoresHard.add(score);
                Collections.sort(scoresHard);
                break;
        }
    }

    public void clearAllScores(Level level) {
        switch (level) {
            case EASY:
                scoresEasy.clear();
                break;
            case MEDIUM:
                scoresMedium.clear();
                break;
            case HARD:
                scoresHard.clear();
                break;
        }
    }

    public boolean isBestScore(int time, Level level) {
        switch (level) {
            case EASY:
                return checkScore(time, scoresEasy);
            case MEDIUM:
                return checkScore(time, scoresMedium);
            case HARD:
                return checkScore(time, scoresHard);
        }
        return false;
    }

    private boolean checkScore(int time, List<Score> list) {
        return list.size() < SCORES_SIZE
                || time < list.get(list.size() - 1).getTime();
    }

    public void loadBestScores() {
        Path pathEasyLevel = Paths.get(fileNameForEasyLevel);
        Path pathMediumLevel = Paths.get(fileNameForMediumLevel);
        Path pathHardLevel = Paths.get(fileNameForHardLevel);

        readScoresFromFile(pathEasyLevel, Level.EASY);
        readScoresFromFile(pathMediumLevel, Level.MEDIUM);
        readScoresFromFile(pathHardLevel, Level.HARD);
    }

    private void readScoresFromFile(Path path, Level level) {
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (DataInputStream br = new DataInputStream(Files.newInputStream(path))) {
            while (br.available() > 0) {
                String name = br.readUTF();
                int time = br.readInt();

                Score score = new Score(name, time);

                switch (level) {
                    case EASY:
                        scoresEasy.add(score);
                        break;
                    case MEDIUM:
                        scoresMedium.add(score);
                        break;
                    case HARD:
                        scoresHard.add(score);
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Couldn't load the results...");
        }
    }

    public void storeBestScores() {
        Path pathEasyLevel = Paths.get(fileNameForEasyLevel);
        Path pathMediumLevel = Paths.get(fileNameForMediumLevel);
        Path pathHardLevel = Paths.get(fileNameForHardLevel);

        writeScoresToFile(pathEasyLevel, Level.EASY);
        writeScoresToFile(pathMediumLevel, Level.MEDIUM);
        writeScoresToFile(pathHardLevel, Level.HARD);
    }

    private void writeScoresToFile(Path path, Level level) {

        try (DataOutputStream bw =
                     new DataOutputStream(new BufferedOutputStream(Files.newOutputStream(path)))) {
            Iterator<Score> iterator = scoresEasy.iterator();
            switch (level) {
                case EASY:
                    iterator = scoresEasy.iterator();
                    break;
                case MEDIUM:
                    iterator = scoresMedium.iterator();
                    break;
                case HARD:
                    iterator = scoresHard.iterator();
                    break;
            }

            while (iterator.hasNext()) {
                Score score = iterator.next();

                bw.writeUTF(score.getName());
                bw.writeInt(score.getTime());

            }
        } catch (IOException e) {
            System.out.println("Couldn't save the result...");
        }
    }

    public ObservableList<Score> getScores(Level level) {
        switch (level) {
            case MEDIUM:
                return FXCollections.unmodifiableObservableList(scoresMedium);
            case HARD:
                return FXCollections.unmodifiableObservableList(scoresHard);
            default:
                return FXCollections.unmodifiableObservableList(scoresEasy);
        }
    }
}

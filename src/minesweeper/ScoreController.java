package minesweeper;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import minesweeper.model.Score;

public class ScoreController {

    @FXML
    private TextField nameField;
    @FXML
    private Label timeLabel;

    void processResults(String time) {
        timeLabel.setText(time);
    }

    Score addScore() {

        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            return null;
        }
        int time = Integer.valueOf(timeLabel.getText());
        return new Score(name, time);
    }

}

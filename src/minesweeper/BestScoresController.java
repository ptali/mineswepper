package minesweeper;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import minesweeper.model.Level;
import minesweeper.model.Score;
import minesweeper.model.ScoreData;

import java.util.Optional;

public class BestScoresController {

    @FXML
    private TableView<Score> scoresTable;
    @FXML
    private TableColumn<String, Integer> tableColumn;
    @FXML
    private ToggleButton buttonEasy;
    @FXML
    private ToggleButton buttonMedium;
    @FXML
    private ToggleGroup groupButtons;

    private ScoreData data;

    public void initialize() {
        data = ScoreData.getInstance();
        scoresTable.setItems(data.getScores(Level.EASY));
        setColumn();
    }

    private void setColumn() {
        tableColumn.setCellFactory(col -> {
            TableCell<String, Integer> indexCell = new TableCell<>();
            ReadOnlyObjectProperty<TableRow<String>> rowProperty = indexCell.tableRowProperty();
            ObjectBinding<String> rowBinding = Bindings.createObjectBinding(() -> {
                TableRow<String> row = rowProperty.get();
                if (row != null) {
                    int rowIndex = row.getIndex() + 1;
                    if (rowIndex < row.getTableView().getItems().size() + 1) {
                        return Integer.toString(rowIndex);
                    }
                }
                return null;
            }, rowProperty);
            indexCell.textProperty().bind(rowBinding);
            return indexCell;
        });
    }

    @FXML
    public void showClearAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Reset best scores");
        alert.setHeaderText("Reset all scores");
        alert.setContentText("Are you sure?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (groupButtons.getSelectedToggle().equals(buttonEasy)) {
                data.clearAllScores(Level.EASY);
                return;
            }
            if (groupButtons.getSelectedToggle().equals(buttonMedium)) {
                data.clearAllScores(Level.MEDIUM);
                return;
            }
            data.clearAllScores(Level.HARD);
        }
    }

    @FXML
    public void handleButtonEasy() {
        if (groupButtons.selectedToggleProperty().isNull().get()) {
            buttonEasy.setSelected(true);
        }
        scoresTable.setItems(data.getScores(Level.EASY));
        scoresTable.refresh();
    }

    @FXML
    public void handleButtonMedium() {
        if (groupButtons.selectedToggleProperty().isNull().get()) {
            handleButtonEasy();
            return;
        }
        scoresTable.setItems(data.getScores(Level.MEDIUM));
        scoresTable.refresh();
    }

    @FXML
    public void handleButtonHard() {
        if (groupButtons.selectedToggleProperty().isNull().get()) {
            handleButtonEasy();
            return;
        }
        scoresTable.setItems(data.getScores(Level.HARD));
        scoresTable.refresh();
    }
}

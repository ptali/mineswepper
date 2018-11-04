package minesweeper;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import minesweeper.model.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Optional;

public class Controller implements PropertyChangeListener {

    private static final int IMAGE_SIZE = 30;
    static Stage stage = new Stage();
    private ImageView[][] imageView;
    private Game game;
    private Timeline timeline;
    private ScoreData score;

    @FXML
    public ImageView faceImage;
    @FXML
    public ImageView numberOfBombs1;
    @FXML
    public ImageView numberOfBombs2;
    @FXML
    public ImageView numberOfBombs3;
    @FXML
    public ImageView gameTime1;
    @FXML
    public ImageView gameTime2;
    @FXML
    public ImageView gameTime3;
    @FXML
    private GridPane gridPane;

    public void initialize() {
        this.game = new Game();
        this.score = ScoreData.getInstance();
        loadStateImages();
        loadTimeImages();
        loadGrid();
        startTime();
        updateBombsView();
    }

    private void loadGrid() {
        int sizeX = game.getSizeX();
        int sizeY = game.getSizeY();
        this.imageView = new ImageView[sizeX][sizeY];
        Image image;

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                image = (Image) game.getBoard().getMatrix()[x][y].getState().image;
                imageView[x][y] = new ImageView(image);
                game.getBoard().getMatrix()[x][y].addPropertyChangeListener(this);
                gridPane.add(imageView[x][y], x, y);
            }
        }
    }

    private void startTime() {
        timeline = new Timeline(new KeyFrame(
                Duration.millis(1000),
                actionEvent -> {
                    game.addSecond();
                    updateTimeView();
                }));
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    private void updateTimeView() {
        gameTime3.setImage(getImageTime(game.getTime() % 10));
        gameTime2.setImage(getImageTime(game.getTime() / 10 % 10));
        gameTime1.setImage(getImageTime(game.getTime() / 100 % 10));
    }

    private void updateBombsView() {
        numberOfBombs3.setImage(getImageTime(game.getBombsCounter() % 10));
        numberOfBombs2.setImage(getImageTime(game.getBombsCounter() / 10 % 10));
    }

    private void loadStateImages() {
        for (State state : State.values()) {
            state.image = (getImage(state.name().toLowerCase()));
        }
    }

    private void loadTimeImages() {
        for (GameTime time : GameTime.values()) {
            time.image = (getImage("time/" + time.name().toLowerCase()));
        }
    }

    private Image getImage(String name) {
        String fileName = "/" + name + ".png";
        return new Image(getClass().getResourceAsStream(fileName));
    }

    private Image getImageTime(int i) {
        switch (i) {
            case 1:
                return (Image) GameTime.ONE.image;
            case 2:
                return (Image) GameTime.TWO.image;
            case 3:
                return (Image) GameTime.THREE.image;
            case 4:
                return (Image) GameTime.FOUR.image;
            case 5:
                return (Image) GameTime.FIVE.image;
            case 6:
                return (Image) GameTime.SIX.image;
            case 7:
                return (Image) GameTime.SEVEN.image;
            case 8:
                return (Image) GameTime.EIGHT.image;
            case 9:
                return (Image) GameTime.NINE.image;
        }
        return (Image) GameTime.ZERO.image;
    }

    @FXML
    public void handleExit() {
        Platform.exit();
    }

    @FXML
    public void openNewGame() {
        Level level = game.getLevel();

        switch (level) {

            case EASY:
                startEasyLevel();
                break;

            case MEDIUM:
                startMediumLevel();
                break;

            case HARD:
                startHardLevel();
                break;
        }
    }

    @FXML
    public void startEasyLevel() {
        changeGameLevel(Level.EASY);
    }

    @FXML
    public void startMediumLevel() {
        changeGameLevel(Level.MEDIUM);
    }

    @FXML
    public void startHardLevel() {
        changeGameLevel(Level.HARD);
    }

    private void changeGameLevel(Level level) {
        faceImage.setImage(new Image(getClass().getResourceAsStream("/faces/yellowface.png")));
        gridPane.getChildren().clear();

        game = new Game(level);
        timeline.stop();
        updateTimeView();
        updateBombsView();
        loadGrid();

        stage.sizeToScene();
        stage.centerOnScreen();
    }

    @FXML
    public void openScoresView() {
        Stage stage = new Stage();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("bestscores.fxml"));
            stage.setScene(new Scene(root));
            root.requestFocus();

        } catch (IOException ex) {
            System.out.println("Couldn't load the scores view");
            ex.printStackTrace();
            return;
        }

        stage.setTitle("Best scores");
        stage.getIcons().add(new Image("icon.png"));
        stage.setResizable(false);
        stage.initOwner(Controller.stage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    public void handleClick(MouseEvent mouseEvent) {
        if (game.getState().equals(GameState.CLOSED)) {
            game.start();
            timeline.play();
        }

        if (game.getState().equals(GameState.BOMB) || game.getState().equals(GameState.WIN)) {
            return;
        }

        int x = (int) (mouseEvent.getX() / IMAGE_SIZE);
        int y = (int) (mouseEvent.getY() / IMAGE_SIZE);
        Cell cell = game.getBoard().getMatrix()[x][y];

        switch (mouseEvent.getButton()) {
            case PRIMARY:
                pressLeftButton(cell);
                break;

            case SECONDARY:
                pressRightButton(cell);
                break;
        }

        if (game.getState().equals(GameState.BOMB)) {
            timeline.stop();
            faceImage.setImage(new Image(getClass().getResourceAsStream("/faces/gameoverface.png")));
        }
        if (game.getState().equals(GameState.WIN)) {
            timeline.stop();
            faceImage.setImage(new Image(getClass().getResourceAsStream("/faces/winface.png")));
            if (score.isBestScore(game.getTime(), game.getLevel())) {
                showAddScoreDialog();
            }
        }
        updateBombsView();
    }

    private void pressLeftButton(Cell cell) {
        game.openCell(cell);
    }

    private void pressRightButton(Cell cell) {
        game.markCell(cell);
    }

    private void showAddScoreDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(gridPane.getScene().getWindow());
        dialog.setTitle("Congratulations!");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("score.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException ex) {
            System.out.println("Couldn't load the dialog");
            ex.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        ScoreController controller = fxmlLoader.getController();
        controller.processResults(String.valueOf(game.getTime()));

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Score newScore = controller.addScore();
            if (newScore != null) {
                score.addScore(newScore, game.getLevel());
            }
        }
    }

    @FXML
    public void handleClickFaceButton() {
        openNewGame();
    }

    @FXML
    public void handleMousePressed() {
        faceImage.setImage(new Image(getClass().getResourceAsStream("/faces/clickedface.png")));
    }

    @FXML
    public void handleMouseReleased() {
        faceImage.setImage(new Image(getClass().getResourceAsStream("/faces/yellowface.png")));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Object source = evt.getSource();
        String evtName = evt.getPropertyName();

        if (source.getClass() == (Cell.class)) {
            Cell cell = (Cell) source;
            State state = State.valueOf(evtName);
            imageView[cell.getX()][cell.getY()].setImage((Image) state.image);
        }
    }
}

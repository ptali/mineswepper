package minesweeper;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import minesweeper.controller.Controller;
import minesweeper.data.ScoreData;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage = Controller.stage;
        Parent root = FXMLLoader.load(getClass().getResource("mainwindow.fxml"));
        primaryStage.setTitle("Minesweeper");
        primaryStage.getIcons().add(new Image("icon.png"));

        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void stop() {
        ScoreData.getInstance().storeBestScores();
    }

    @Override
    public void init() {
        ScoreData.getInstance().loadBestScores();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

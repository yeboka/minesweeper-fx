package mukan.minesweeperfx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage stage;
    private GameField gameField;
    private Scene game;
    private Scene menu;
    private VBox gamePane;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        Image image = new Image("C:\\Users\\acer\\IdeaProjects\\minesweeper-fx\\src\\main\\java\\mukan\\minesweeperfx\\icon.png");
        stage.getIcons().add(image);
        stage.setTitle("Minesweeper");
        stage.setResizable(false);
        stage.show();

        StackPane stackPane = new StackPane();
        VBox vPane = new VBox();
        vPane.setSpacing(10);
        vPane.setMinHeight(450);
        vPane.setMinWidth(400);
        vPane.setAlignment(Pos.CENTER);
        // menu scene
        Text header = new Text("MINESWEEPER");
        header.setStyle("-fx-font: normal bold 30px 'serif'; -fx-fill: BLUE");

        Button startBtn = new Button("START");
        startBtn.setStyle("-fx-min-width: 50px;");

        vPane.getChildren().add(header);
        vPane.getChildren().add(startBtn);
        vPane.setAlignment(Pos.CENTER);
        stackPane.getChildren().add(vPane);

        menu = new Scene(stackPane);


        //game scene
        StopWatch stopWatch = new StopWatch();
        Text flags = new Text("Flags: 10");
        Button restart = new Button("\uD83D\uDE00");
        restart.setOnAction((e) -> {
            restartGame(flags, stopWatch);
        });

        gamePane = new VBox();
        HBox bar = new HBox();

        bar.getChildren().addAll(flags, restart, stopWatch.getCurrTime());
        bar.setSpacing(50);
        bar.setAlignment(Pos.CENTER);

        gameField = new GameField(flags, stopWatch);
        gameField.createField();

        gamePane.getChildren().addAll(bar, gameField.getField());

        game = new Scene(gamePane);
        startBtn.setOnAction(e -> {
            primaryStage.setScene(game);
            stopWatch.start();
        });

        stage.setScene(menu);
    }

    private void restartGame(Text flags, StopWatch stopWatch) {
        gameField = new GameField(flags, stopWatch);
        gameField.createField();

        stopWatch.restart();

        gamePane.getChildren().remove(1);
        gamePane.getChildren().add(gameField.getField());
    }

    public static void main(String[] args) {
        launch();
    }
}

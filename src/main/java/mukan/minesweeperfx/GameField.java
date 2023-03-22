package mukan.minesweeperfx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GameField {
    private final Cell[][] field = new Cell[10][10];
    private final StackPane gridWrapper;

    int[][] bombsCoordinates;
    int[][] dir = new int[][]{{-1, -1}, {-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}};
    private boolean isFirstClick = false;
    private int flagCount = 10;
    private Text flagsText;
    private boolean gameIsEnd;
    private int cuntOfVisitedCells = 0;
    private StopWatch stopWatch;

    public GameField(Text falgsText, StopWatch stopWatch) {
        this.stopWatch = stopWatch;
        this.flagsText = falgsText;
        this.gridWrapper = new StackPane();
        this.gridWrapper.setPadding(new Insets(5, 5, 5, 5));
        this.gridWrapper.setAlignment(Pos.CENTER);
    }

    public void createField () {
        this.gameIsEnd = false;

        GridPane grid = new GridPane();

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                int a = i;
                int b = j;
                field[i][j] = new Cell(i, j);
                field[i][j].getStackPane().setOnMouseClicked((e) -> {
                    if (e.getButton() == MouseButton.PRIMARY)
                        click(a, b);
                    else
                        flag(a, b);
                });
                grid.add(getCell(i, j).getStackPane(), i, j);
            }
        }
        this.gridWrapper.getChildren().add(grid);

    }

    public Cell getCell (int i, int j) {
        return field[i][j];
    }

    public Pane getField() {
        return gridWrapper;
    }

    public void click (int i, int j) {
        if (gameIsEnd) return;

        if (!isFirstClick) {
            isFirstClick = true;
            setBombs(i, j);
        }
        if (field[i][j].isFlagged()) return;
        if (field[i][j].isVisited()) {
            openNeighbors(i, j);
            System.out.println("open neighbors");
        }
        if (field[i][j].isMine()) showMines();

        System.out.println("click! " + i + " " + j);

        openCells(i, j);
        if  (cuntOfVisitedCells == ((field.length * field[0].length) - bombsCoordinates.length)) playerWin();
    }

    private void showMines() {
        this.gameIsEnd = true;
        for (int[] pos : bombsCoordinates) {
            field[pos[0]][pos[1]].setBombVisible();
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        stopWatch.stop();

        Text time = new Text(stopWatch.getCurrTime().getText());
        time.setStyle("-fx-fill: #ff0000; -fx-font-size: 30px; -fx-alignment: center; -fx-font-weight: bold");

        Text gameOverText = new Text("You Loose :/");
        gameOverText.setStyle("-fx-text-fill: #9c00cb; -fx-font-size: 60px; -fx-alignment: center; -fx-font-weight: bold");

        VBox vBox = new VBox(gameOverText, time);
        vBox.setAlignment(Pos.CENTER);
        this.gridWrapper.getChildren().add(vBox);
    }

    public void openCells (int i, int j) {
        if (!isValid(i, j)) return;
        if (field[i][j].isMine()) return;

        field[i][j].setVisited(true);
        cuntOfVisitedCells++;

        if (field[i][j].isFlagged()) {
            flagCount++;
            field[i][j].setFlagged(false);
        }

        int cnt = countOfBombsAround(i, j);
        field[i][j].setBombCount(cnt);
        if (cnt != 0) return;

        for (int[] a : dir) openCells(i + a[0], j + a[1]);
    }

    public void openNeighbors (int i, int j) {
        int countOfNeutralizedBombs = 0;
        int countOfBombsAround = countOfBombsAround(i, j);
        for (int[] pair : dir) {
            int ix = i + pair[0];
            int jx = j + pair[1];
            if (ix  < 0 || ix >= field.length || jx < 0 || jx >= field[i].length) continue;
            if (field[ix][jx].isMine() && field[ix][jx].isFlagged()) {
                countOfNeutralizedBombs++;
            }
        }

        if (countOfNeutralizedBombs != countOfBombsAround) {
            return;
        }

        for (int[] pair : dir) {
            int ix = i + pair[0];
            int jx = j + pair[1];
            if (ix  < 0 || ix >= field.length || jx < 0 || jx >= field[i].length) continue;
            if (!field[ix][jx].isVisited() && !field[ix][jx].isMine()) {
                openCells(ix, jx);
            }
        }

    }

    boolean isValid (int i, int j) {
        if (i < 0 || i >= field.length || j < 0 || j >= field[i].length) return false;
        return !field[i][j].isVisited();
    }

    int countOfBombsAround (int i, int j) {
        int count = 0;

        for (int[] a : dir) {
            int ix = i + a[0];
            int jx = j + a[1];

            if (jx >= 0 && ix >= 0 && ix < field.length && jx < field[0].length
                    && field[ix][jx].isMine()) {
                count++;
            }
        }

        return count;
    }

    public void flag (int i, int j) {
        if (field[i][j].isVisited()) return;

        if (field[i][j].isFlagged() && flagCount == 0) {
            field[i][j].setFlagged(false);
            flagCount++;
            this.flagsText.setText("Flags: " + flagCount);
            return;
        }

        if (field[i][j].isFlagged()){
            if (flagCount == 0) return;
            field[i][j].setFlagged(false);
            flagCount++;
        } else {
            if (flagCount == 0) return;
            field[i][j].setFlagged(true);
            flagCount--;
        }
        this.flagsText.setText("Flags: " + flagCount);
        System.out.println("flagged! count of flags: " + flagCount);
    }

    private void playerWin() {
        Text winnerText = new Text("WINNER");
        Text time = new Text(stopWatch.getCurrTime().getText());
        time.setStyle("-fx-fill: #ff0000; -fx-font-size: 30px; -fx-alignment: center; -fx-font-weight: bold");
        winnerText.setStyle("-fx-fill: #0051ff; -fx-font-size: 60px; -fx-alignment: center; -fx-font-weight: bold");

        stopWatch.stop();

        VBox vBox = new VBox(winnerText, time);
        vBox.setAlignment(Pos.CENTER);
        this.gridWrapper.getChildren().add(vBox);
    }

    public void setBombs (int i, int j) {
         bombsCoordinates = generateBombs(i, j);

        for (int[] pair : bombsCoordinates) {
            field[pair[0]][pair[1]].setAsMine();
        }
    }

    private static int[][] generateBombs (int i, int j) {
        Random random = new Random();
        int numberOfBombs = 10;
        int[][] bombsCoordinates = new int[numberOfBombs][2];

        Set<Integer> uniqueCoordinates = new HashSet<>();
        int idx = 0;
        while (idx < numberOfBombs && uniqueCoordinates.size() <= numberOfBombs) {
            int y = random.nextInt(10);
            int x = random.nextInt(10);
            if (y == i && x == j) continue;
            if (uniqueCoordinates.add(y*y - x*x)) {
                int[] pair = {y, x};
                bombsCoordinates[idx++] = pair;
            }
        }

        for (int[] pair : bombsCoordinates) {
            System.out.println(pair[0] + " " + pair[1]);
        }

        return bombsCoordinates;
    }


}

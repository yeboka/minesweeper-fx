package mukan.minesweeperfx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
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
    private boolean gameIsEnd;
    private int cuntOfVisitedCells = 0;

    public GameField() {
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
        System.out.println("click! " + i + " " + j);
        if (field[i][j].isMine()) {
            showMines();
        }
        openCells(i, j);
        if  (cuntOfVisitedCells == ((field.length * field[0].length) - bombsCoordinates.length)) playerWinOrNot();
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

        Text gameOverText = new Text("You Loose :/");
        gameOverText.setStyle("-fx-text-fill: #9c00cb; -fx-font-size: 60px; -fx-alignment: center; -fx-font-weight: bold");
        this.gridWrapper.getChildren().add(gameOverText);
    }

    void openCells (int i, int j) {
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
        System.out.println("flagged! count of flags: " + flagCount);
    }

    private void playerWinOrNot() {
        Text winnerText = new Text("WINNER");
        winnerText.setStyle("-fx-fill: #0051ff; -fx-font-size: 60px; -fx-alignment: center; -fx-font-weight: bold");
        this.gridWrapper.getChildren().add(winnerText);
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

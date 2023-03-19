package mukan.minesweeperfx;

import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Cell {
    private StackPane stackPane;
    private int x;
    private int y;
    private boolean isMine;
    private boolean isFlagged;
    private int bombCount;
    private boolean isVisited;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.stackPane = new StackPane();
        this.isFlagged = false;
        this.isVisited = false;
        this.bombCount = 0;

        this.stackPane.setStyle("-fx-min-width: 39px; -fx-min-height: 39px; -fx-background-color: #34e1eb; -fx-border-color: black; -fx-border-width: 1px");
        this.stackPane.setPadding(new Insets(3, 3, 3, 3));

    }

    public StackPane getStackPane() {
        return stackPane;
    }
    public void setAsMine() {
//        this.stackPane.setStyle("-fx-background-color: RED");
        this.isMine = true;
    }

    public boolean isMine() {
        return isMine;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public int getBombCount() {
        return bombCount;
    }

    public void setBombCount(int count) {
        Text text = new Text(String.valueOf(count));
        text.setStyle("-fx-font-weight: bold; -fx-font-size: 15px");
        setFlagged(false);
        this.stackPane.getChildren().clear();
        if (count == 0) {
            this.stackPane.setStyle("-fx-min-width: 39px; -fx-min-height: 39px; -fx-background-color: #fffe90; -fx-border-color: black; -fx-border-width: 1px");
        }
        if (count == 1) {
            text.setFill(Color.BLUE);
        }
        if (count == 2) {
            text.setFill(Color.GREEN);
        }
        if (count == 3) {
            text.setFill(Color.RED);
        }
        if (count == 4) {
            text.setFill(Color.DARKBLUE);
        }
        if (count == 5) {
            text.setFill(Color.BROWN);
        }
        if (count == 6) {
            text.setFill(Color.rgb(0, 186, 180));
        }
        if (count != 0) {
            this.stackPane.setStyle("-fx-min-width: 39px; -fx-min-height: 39px; -fx-background-color: #fcfa69; -fx-border-color: black; -fx-border-width: 1px");
            this.stackPane.getChildren().add(text);
        }
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setFlagged(boolean flagged) {
        Text text = new Text("F");
        text.setStyle("-fx-font-weight: bold; -fx-font-size: 15px; -fx-fill: RED");
        if (!isFlagged)
            this.stackPane.getChildren().add(text);
        else
            this.stackPane.getChildren().clear();
        isFlagged = flagged;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    public void setBombVisible() {
        this.stackPane.getChildren().clear();
        Text bombText = new Text("*");
        bombText.setStyle("-fx-font-weight: bold; -fx-font-size: 20px; -fx-fill: BLACK;");
        this.stackPane.getChildren().add(bombText);
        this.stackPane.setStyle("-fx-background-color: #f34534");
    }
}
